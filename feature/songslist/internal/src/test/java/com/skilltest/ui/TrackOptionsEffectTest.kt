package com.skilltest.ui

import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track
import com.skilltest.domain.repository.SongRepository
import com.skilltest.domain.usecase.GetRecentlyPlayedUseCase
import com.skilltest.domain.usecase.SearchSongsByTermUseCase
import com.skilltest.domain.usecase.ShouldShowSearchLoadingUseCase
import com.skilltest.ui.model.UiTrack
import com.skilltest.ui.viewmodel.SongsListEffect
import com.skilltest.ui.viewmodel.SongsListIntent
import com.skilltest.ui.viewmodel.SongsListReducer
import com.skilltest.ui.viewmodel.SongsListState
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class TrackOptionsEffectTest {
    @Test
    fun trackOptionsCarriesSelectedAlbumAndSongInfo() = runBlocking {
        val selectedTrack = UiTrack(
            id = "track-5",
            name = "Thriller",
            artistName = "Michael Jackson",
            artwork = "",
            isExplicit = false,
            artistId = "artist-7",
            albumId = "album-8",
        )
        val repository = object : SongRepository {
            override suspend fun searchSongsByTerm(term: String) =
                ResultWrapper.Success(SearchResult(emptyList()))

            override suspend fun addToRecentlyPlayed(track: Track) =
                ResultWrapper.Success(Unit)

            override fun getRecentlyPlayed() =
                flowOf(ResultWrapper.Success(RecentlyPlayed(emptyList())))
        }
        val reducer = SongsListReducer(
            SearchSongsByTermUseCase(repository),
            ShouldShowSearchLoadingUseCase(),
            GetRecentlyPlayedUseCase(repository),
        )

        val reduction = reducer.reduce(
            SongsListState(searchResult = listOf(selectedTrack)),
            SongsListIntent.OpenTrackMoreOptions(selectedTrack),
        )

        assertEquals(
            SongsListEffect.NavigateToTrackOptions(selectedTrack),
            reduction.effects.single(),
        )

        val withoutAlbumId = selectedTrack.copy(albumId = "")
        val missingAlbumReduction = reducer.reduce(
            SongsListState(searchResult = listOf(withoutAlbumId)),
            SongsListIntent.OpenTrackMoreOptions(withoutAlbumId),
        )
        assertEquals(
            SongsListEffect.NavigateToTrackOptions(withoutAlbumId),
            missingAlbumReduction.effects.single(),
        )
    }
}
