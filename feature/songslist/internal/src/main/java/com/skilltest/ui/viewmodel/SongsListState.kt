package com.skilltest.ui.viewmodel

import androidx.compose.runtime.Immutable
import com.skilltest.core.presentation.viewmodel.BaseUiState
import com.skilltest.core.presentation.viewmodel.PartialStateChange
import com.skilltest.domain.model.Track
import com.skilltest.ui.model.UiTrack
import com.skilltest.ui.model.UiTrack.Companion.toUi

@Immutable
data class SongsListState(
    val stateName: String = "SongsListState",
    val isLoading: Boolean = false,
    val isLoadingSearch: Boolean = false,
    val isLoadingRecentlyPlayed: Boolean = false,
    val searchResult: List<UiTrack> = emptyList(),
    val searchTerm: String = "",
    val lastSearchedTerm: String? = null,
    val recentlyPlayed: List<UiTrack> = emptyList(),
    val isError: Boolean = false,
    val isSearching: Boolean = false,
) : BaseUiState(stateName)

internal sealed interface SongsListStateChange : PartialStateChange<SongsListState> {
    data object SearchCleared : SongsListStateChange {
        override fun reduce(previous: SongsListState): SongsListState = previous.copy(
            searchTerm = "",
            lastSearchedTerm = null,
            isLoadingSearch = false,
            searchResult = emptyList()
        )
    }

    data object StartSearching : SongsListStateChange {
        override fun reduce(previous: SongsListState): SongsListState = previous.copy(
            isSearching = true
        )
    }

    data class LoadedRecentlyPlayed(
        val results: List<Track>
    ) : SongsListStateChange {
        override fun reduce(previous: SongsListState): SongsListState = previous.copy(
            recentlyPlayed = results.map { it.toUi() },
            isLoadingRecentlyPlayed = false
        )
    }

    data class LoadedSearchResults(
        val term: String,
        val results: List<Track>
    ) : SongsListStateChange {
        override fun reduce(previous: SongsListState): SongsListState =
            if (previous.searchTerm != term) previous else previous.copy(
                searchResult = results.map { it.toUi() },
                isLoadingSearch = false,
                lastSearchedTerm = term,
            )
    }

    data class SearchTermChanged(val term: String) : SongsListStateChange {
        override fun reduce(previous: SongsListState): SongsListState = previous.copy(
            searchTerm = term,
            lastSearchedTerm = null,
            searchResult = emptyList(),
            isLoadingSearch = false,
        )
    }

    data class TrackListLoading(val recentlyPlayed: Boolean? = null, val search: Boolean? = null) :
        SongsListStateChange {
        override fun reduce(previous: SongsListState): SongsListState = previous.copy(
            isLoadingSearch = search ?: previous.isLoadingSearch,
            isLoadingRecentlyPlayed = recentlyPlayed ?: previous.isLoadingRecentlyPlayed
        )
    }
}
