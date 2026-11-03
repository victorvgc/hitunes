package com.skilltest.data.local

import androidx.room.Room
import com.skilltest.data.local.database.HiTunesDatabase
import com.skilltest.data.local.database.RecentlyPlayedDao
import com.skilltest.data.local.model.RecentlyPlayedTrackEntity
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class LocalSongDataSourceTest {
    private lateinit var database: HiTunesDatabase
    private var now = 0L
    private lateinit var dataSource: LocalSongDataSource

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication(),
            HiTunesDatabase::class.java,
        ).allowMainThreadQueries().build()
        dataSource = LocalSongDataSource(database.recentlyPlayedDao()) { now }
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun emptyDatabaseReturnsNoTracks() = runTest {
        val result = dataSource.fetchRecentlyPlayedSongs().first()

        assertTrue(result is ResultWrapper.Success)
        assertTrue(result.data!!.trackList.isEmpty())
    }

    @Test
    fun savedTrackIsReadBackWithAllFields() = runTest {
        now = 100L
        dataSource.saveRecentlyPlayedSong(track("1"))

        assertEquals(
            listOf(track("1")),
            dataSource.fetchRecentlyPlayedSongs().first().data!!.trackList
        )
    }

    @Test
    fun activeObserverReceivesSavedTrackWithoutRefetching() = runBlocking {
        val updates = Channel<List<Track>>(Channel.UNLIMITED)
        val collector = launch(Dispatchers.Default) {
            dataSource.fetchRecentlyPlayedSongs().collect { result ->
                updates.send(result.data!!.trackList)
            }
        }
        try {
            assertEquals(emptyList<Track>(), withTimeout(5_000) { updates.receive() })
            now = 100L
            dataSource.saveRecentlyPlayedSong(track("1"))
            assertEquals(listOf(track("1")), withTimeout(5_000) { updates.receive() })
        } finally {
            collector.cancel()
        }
    }

    @Test
    fun tracksAreOrderedByMostRecentlyPlayedFirst() = runTest {
        now = 100L
        dataSource.saveRecentlyPlayedSong(track("1"))
        now = 200L
        dataSource.saveRecentlyPlayedSong(track("2"))
        now = 300L
        dataSource.saveRecentlyPlayedSong(track("3"))

        assertEquals(
            listOf("3", "2", "1"),
            dataSource.fetchRecentlyPlayedSongs().first().data!!.trackList.map { it.id },
        )
    }

    @Test
    fun replayingATrackMovesItToTheTopWithoutDuplicating() = runTest {
        now = 100L
        dataSource.saveRecentlyPlayedSong(track("1"))
        now = 200L
        dataSource.saveRecentlyPlayedSong(track("2"))
        now = 300L
        dataSource.saveRecentlyPlayedSong(track("1"))

        assertEquals(
            listOf("1", "2"),
            dataSource.fetchRecentlyPlayedSongs().first().data!!.trackList.map { it.id },
        )
        assertEquals(
            300L,
            database.recentlyPlayedDao().getAllByMostRecent().first().first().lastPlayed
        )
    }

    @Test
    fun databaseFailureIsReturnedAsError() = runTest {
        val failingDao = object : RecentlyPlayedDao {
            override suspend fun upsert(track: RecentlyPlayedTrackEntity): Unit =
                throw IllegalStateException("disk error")

            override fun getAllByMostRecent() = flow<List<RecentlyPlayedTrackEntity>> {
                throw IllegalStateException("disk error")
            }
        }
        val failingSource = LocalSongDataSource(failingDao)
        assertTrue(failingSource.fetchRecentlyPlayedSongs().first() is ResultWrapper.Error)
        assertTrue(failingSource.saveRecentlyPlayedSong(track("1")) is ResultWrapper.Error)
    }

    private fun track(id: String) = Track(
        id = id,
        name = "Song $id",
        artistName = "Artist $id",
        albumName = "Album $id",
        artworkUrl = "https://example.test/$id.jpg",
        isExplicit = id == "1",
        artistId = "artist-$id",
        albumId = "album-$id",
        streamUrl = "https://example.test/$id.m4a",
    )
}
