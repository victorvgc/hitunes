package com.skilltest.data.repository

import com.skilltest.data.datasource.SongDataSource
import com.skilltest.domain.model.Failure
import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class SongRepositoryImplTest {
    @Test
    fun searchDelegatesToRemoteAndPreservesResult() = runTest {
        val expected = ResultWrapper.Success(SearchResult(listOf(track)))
        val remote = FakeRemote(expected)
        val repository = SongRepositoryImpl(remote, FakeLocal())

        assertSame(expected, repository.searchSongsByTerm("rock"))
        assertEquals("rock", remote.lastTerm)
    }

    @Test
    fun recentTracksDelegateToLocalAndPreserveFailures() = runTest {
        val local = FakeLocal()
        val repository = SongRepositoryImpl(FakeRemote(ResultWrapper.Success(SearchResult(emptyList()))), local)

        assertSame(local.saveResult, repository.addToRecentlyPlayed(track))
        assertSame(track, local.savedTrack)
        assertSame(local.fetchResult, repository.getRecentlyPlayed())
        assertEquals(1, local.fetchCalls)
    }

    private class FakeRemote(
        private val result: ResultWrapper<SearchResult>,
    ) : SongDataSource.Remote {
        var lastTerm: String? = null
        override suspend fun fetchSearchedSongs(term: String): ResultWrapper<SearchResult> {
            lastTerm = term
            return result
        }
    }

    private class FakeLocal : SongDataSource.Local {
        val saveResult = ResultWrapper.Error<Unit>(Failure(msg = "disk full"))
        val fetchResult = ResultWrapper.Success(RecentlyPlayed(listOf(track)))
        var savedTrack: Track? = null
        var fetchCalls = 0

        override suspend fun saveRecentlyPlayedSong(track: Track): ResultWrapper<Unit> {
            savedTrack = track
            return saveResult
        }

        override suspend fun fetchRecentlyPlayedSongs(): ResultWrapper<RecentlyPlayed> {
            fetchCalls++
            return fetchResult
        }
    }

    private companion object {
        val track = Track("1", "Song", "Artist", "Album", "", false)
    }
}
