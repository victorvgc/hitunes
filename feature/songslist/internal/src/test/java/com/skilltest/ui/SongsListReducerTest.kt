package com.skilltest.ui

import com.skilltest.domain.model.Failure
import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track
import com.skilltest.domain.repository.SongRepository
import com.skilltest.domain.usecase.SearchSongsByTermUseCase
import com.skilltest.domain.usecase.ShouldShowSearchLoadingUseCase
import com.skilltest.ui.viewmodel.SongsListEffect
import com.skilltest.ui.viewmodel.SongsListIntent
import com.skilltest.ui.viewmodel.SongsListReducer
import com.skilltest.ui.viewmodel.SongsListState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SongsListReducerTest {
    private val repository = RecordingRepository()
    private val reducer = SongsListReducer(
        SearchSongsByTermUseCase(repository),
        ShouldShowSearchLoadingUseCase(),
    )

    @Test
    fun clearingSearchDropsResultsAndLoadingFlag() = runTest {
        val state = SongsListState(
            searchTerm = "old",
            isLoadingSearch = true,
            searchResult = listOf(track.toUiTrack()),
        )

        val next = reducer.reduce(state, SongsListIntent.ClearSearch).partialStateChange.reduce(state)

        assertEquals("", next.searchTerm)
        assertEquals(null, next.lastSearchedTerm)
        assertTrue(next.searchResult.isEmpty())
        assertFalse(next.isLoadingSearch)
    }

    @Test
    fun loadingRecentTracksSetsFlagAndQueuesFollowUpIntent() = runTest {
        val state = SongsListState()
        val reduction = reducer.reduce(state, SongsListIntent.LoadRecentlyPlayed)

        assertTrue(reduction.partialStateChange.reduce(state).isLoadingRecentlyPlayed)
        assertEquals(listOf(SongsListIntent.LoadingRecentlyPlayed), reduction.derivedIntents)
    }

    @Test
    fun searchClickStartsSearchMode() = runTest {
        val state = SongsListState()
        val next = reducer.reduce(state, SongsListIntent.SearchClick).partialStateChange.reduce(state)

        assertTrue(next.isSearching)
    }

    @Test
    fun shortTermsDoNotStartSearchButLongTermsQueueIt() = runTest {
        val shortState = SongsListState(searchTerm = "ab")
        val longState = SongsListState(searchTerm = "abc")
        val short = reducer.reduce(shortState, SongsListIntent.StartPerformSearch("ab"))
        val long = reducer.reduce(longState, SongsListIntent.StartPerformSearch("abc"))

        assertFalse(short.partialStateChange.reduce(shortState).isLoadingSearch)
        assertTrue(short.derivedIntents.isEmpty())
        assertTrue(long.partialStateChange.reduce(longState).isLoadingSearch)
        assertEquals(listOf(SongsListIntent.PerformSearch("abc")), long.derivedIntents)
    }

    @Test
    fun typingClearsOldResultsAndWaitsForDebouncedSearch() = runTest {
        val state = SongsListState(
            isLoadingRecentlyPlayed = true,
            isLoadingSearch = true,
            searchResult = listOf(track.toUiTrack()),
            lastSearchedTerm = "old query",
        )
        val short = reducer.reduce(state, SongsListIntent.TypingSearchTerm("ab"))
            .partialStateChange.reduce(state)
        val long = reducer.reduce(state, SongsListIntent.TypingSearchTerm("abc"))
            .partialStateChange.reduce(state)

        assertFalse(short.isLoadingSearch)
        assertEquals("ab", short.searchTerm)
        assertTrue(short.searchResult.isEmpty())
        assertEquals(null, short.lastSearchedTerm)
        assertFalse(long.isLoadingSearch)
        assertEquals("abc", long.searchTerm)
        assertTrue(long.searchResult.isEmpty())
        assertTrue(long.isLoadingRecentlyPlayed)
    }

    @Test
    fun debouncedCallbackIgnoresAnOutdatedQuery() = runTest {
        val state = SongsListState(searchTerm = "abcd")

        val reduction = reducer.reduce(state, SongsListIntent.StartPerformSearch("abc"))

        assertTrue(reduction.derivedIntents.isEmpty())
        assertEquals(state, reduction.partialStateChange.reduce(state))
    }

    @Test
    fun performSearchForwardsTermAndMapsResults() = runTest {
        repository.searchResult = ResultWrapper.Success(SearchResult(listOf(track)))
        val state = SongsListState(searchTerm = "jazz", isLoadingSearch = true)

        val next = reducer.reduce(state, SongsListIntent.PerformSearch("jazz"))
            .partialStateChange.reduce(state)

        assertEquals("jazz", repository.lastSearchTerm)
        assertEquals(listOf("track-1"), next.searchResult.map { it.id })
        assertEquals("album-2", next.searchResult.single().albumId)
        assertEquals("jazz", next.searchTerm)
        assertEquals("jazz", next.lastSearchedTerm)
        assertFalse(next.isLoadingSearch)
    }

    @Test
    fun failedSearchClearsLoadingAndProducesNoResults() = runTest {
        repository.searchResult = ResultWrapper.Error(Failure(msg = "offline"))
        val state = SongsListState(searchTerm = "jazz", isLoadingSearch = true)

        val next = reducer.reduce(state, SongsListIntent.PerformSearch("jazz"))
            .partialStateChange.reduce(state)

        assertTrue(next.searchResult.isEmpty())
        assertFalse(next.isLoadingSearch)
    }

    @Test
    fun olderSearchResponseCannotReplaceNewerQuery() = runTest {
        repository.searchResult = ResultWrapper.Success(SearchResult(listOf(track)))
        val state = SongsListState(searchTerm = "new query", isLoadingSearch = true)

        val next = reducer.reduce(state, SongsListIntent.PerformSearch("old query"))
            .partialStateChange.reduce(state)

        assertEquals("new query", next.searchTerm)
        assertTrue(next.searchResult.isEmpty())
    }

    @Test
    fun returningToScreenDoesNotRepeatCompletedSearch() = runTest {
        val state = SongsListState(searchTerm = "jazz", lastSearchedTerm = "jazz")

        val reduction = reducer.reduce(state, SongsListIntent.StartPerformSearch("jazz"))

        assertTrue(reduction.derivedIntents.isEmpty())
        assertEquals(state, reduction.partialStateChange.reduce(state))
    }

    @Test
    fun trackDetailsProducesNavigationEffect() = runTest {
        val uiTrack = track.toUiTrack()
        val reduction = reducer.reduce(SongsListState(), SongsListIntent.OpenTrackDetails(uiTrack))
        assertEquals(SongsListEffect.NavigateToTrackDetails(uiTrack), reduction.effects.single())
    }

    @Test
    fun recentTracksLoadIntoStateWithAlbumAndArtistIds() = runTest {
        val state = SongsListState(isLoadingRecentlyPlayed = true)
        val next = reducer.reduce(state, SongsListIntent.LoadingRecentlyPlayed)
            .partialStateChange.reduce(state)

        assertEquals(10, next.recentlyPlayed.size)
        assertEquals("album-1", next.recentlyPlayed.first().albumId)
        assertEquals("artist-1", next.recentlyPlayed.first().artistId)
        assertFalse(next.isLoadingRecentlyPlayed)
    }

    private class RecordingRepository : SongRepository {
        var lastSearchTerm: String? = null
        var searchResult: ResultWrapper<SearchResult> = ResultWrapper.Success(SearchResult(emptyList()))

        override suspend fun searchSongsByTerm(term: String): ResultWrapper<SearchResult> {
            lastSearchTerm = term
            return searchResult
        }

        override suspend fun addToRecentlyPlayed(track: Track) = ResultWrapper.Success(Unit)
        override suspend fun getRecentlyPlayed() = ResultWrapper.Success(RecentlyPlayed(emptyList()))
    }

    private fun Track.toUiTrack() = com.skilltest.ui.model.UiTrack(
        id = id,
        name = name,
        artistName = artistName,
        artwork = artworkUrl,
        isExplicit = isExplicit,
        artistId = artistId,
        albumId = albumId,
    )

    private companion object {
        val track = Track("track-1", "Song", "Artist", "Album", "", false, "artist-3", "album-2")
    }
}
