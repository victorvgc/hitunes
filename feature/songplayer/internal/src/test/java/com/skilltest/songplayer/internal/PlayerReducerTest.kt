package com.skilltest.songplayer.internal

import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track
import com.skilltest.domain.repository.SongRepository
import com.skilltest.domain.usecase.AddToRecentlyPlayedUseCase
import com.skilltest.songplayer.internal.viewmodel.PlayerIntent
import com.skilltest.songplayer.internal.viewmodel.PlayerReducer
import com.skilltest.songplayer.internal.viewmodel.PlayerState
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class PlayerReducerTest {
    private val repository = RecordingRepository()
    private val reducer = PlayerReducer(AddToRecentlyPlayedUseCase(repository))
    private val track = Track(
        "1",
        "Song",
        "Artist",
        "Album",
        "",
        false,
        streamUrl = "https://example.test/song.m4a"
    )

    @Test
    fun selectingTrackUpdatesStateWhileControllerConnects() = runTest {
        val previous = PlayerState()

        val reduction = reducer.reduce(previous, PlayerIntent.PlayTrack(track))

        assertEquals(track, reduction.partialStateChange.reduce(previous).track)
    }

    @Test
    fun trackTransitionAddsSongToRecentlyPlayed() = runTest {
        reducer.reduce(PlayerState(), PlayerIntent.TrackStarted(track))

        assertEquals(track, repository.lastAddedTrack)
    }

    private class RecordingRepository : SongRepository {
        var lastAddedTrack: Track? = null

        override suspend fun searchSongsByTerm(term: String) =
            ResultWrapper.Success(SearchResult(emptyList()))

        override suspend fun addToRecentlyPlayed(track: Track): ResultWrapper<Unit> {
            lastAddedTrack = track
            return ResultWrapper.Success(Unit)
        }

        override fun getRecentlyPlayed() =
            flowOf(ResultWrapper.Success(RecentlyPlayed(emptyList())))
    }
}
