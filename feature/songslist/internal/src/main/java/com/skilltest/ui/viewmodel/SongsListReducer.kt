package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseReducer
import com.skilltest.core.presentation.viewmodel.IntentSubscription
import com.skilltest.core.presentation.viewmodel.Reduction
import com.skilltest.domain.usecase.GetRecentlyPlayedUseCase
import com.skilltest.domain.usecase.SearchSongsByTermUseCase
import com.skilltest.domain.usecase.ShouldShowSearchLoadingUseCase
import com.skilltest.ui.viewmodel.SongsListEffect.NavigateToTrackDetails
import com.skilltest.ui.viewmodel.SongsListEffect.NavigateToTrackOptions
import com.skilltest.ui.viewmodel.SongsListIntent.PerformSearch
import com.skilltest.ui.viewmodel.SongsListStateChange.LoadedRecentlyPlayed
import com.skilltest.ui.viewmodel.SongsListStateChange.LoadedSearchResults
import com.skilltest.ui.viewmodel.SongsListStateChange.SearchCleared
import com.skilltest.ui.viewmodel.SongsListStateChange.SearchTermChanged
import com.skilltest.ui.viewmodel.SongsListStateChange.StartSearching
import com.skilltest.ui.viewmodel.SongsListStateChange.TrackListLoading
import kotlinx.coroutines.flow.map

class SongsListReducer(
    private val searchSongsByTermUseCase: SearchSongsByTermUseCase,
    private val shouldShowSearchLoadingUseCase: ShouldShowSearchLoadingUseCase,
    private val getRecentlyPlayedUseCase: GetRecentlyPlayedUseCase,
) : BaseReducer<SongsListState, SongsListIntent, SongsListEffect> {
    override suspend fun reduce(
        previousState: SongsListState,
        intent: SongsListIntent
    ): Reduction<SongsListState, SongsListIntent, SongsListEffect> = when (intent) {
        SongsListIntent.ClearSearch -> {
            Reduction.state(SearchCleared)
        }

        SongsListIntent.LoadRecentlyPlayed -> {
            Reduction(
                partialStateChange = TrackListLoading(recentlyPlayed = true),
                subscriptions = listOf(
                    IntentSubscription(
                        key = "recently-played",
                        intents = getRecentlyPlayedUseCase(Unit).map {
                            SongsListIntent.RecentlyPlayedChanged(
                                it
                            )
                        }
                    )
                )
            )
        }

        is PerformSearch -> {
            val result = searchSongsByTermUseCase(
                SearchSongsByTermUseCase.Request(
                    term = intent.term
                )
            )

            Reduction.state(
                LoadedSearchResults(
                    intent.term,
                    result.data?.trackList.orEmpty()
                )
            )
        }

        is SongsListIntent.OpenTrackDetails -> {
            Reduction.effect(NavigateToTrackDetails(intent.track))
        }

        is SongsListIntent.OpenTrackMoreOptions -> {
            Reduction.effect(NavigateToTrackOptions(intent.track))
        }

        SongsListIntent.SearchClick -> {
            Reduction.state(
                StartSearching
            )
        }

        is SongsListIntent.RecentlyPlayedChanged -> {
            Reduction.state(
                LoadedRecentlyPlayed(intent.result.data?.trackList.orEmpty())
            )
        }

        is SongsListIntent.StartPerformSearch -> {
            val shouldSearch = shouldShowSearchLoadingUseCase(
                ShouldShowSearchLoadingUseCase.Request(
                    intent.term,
                    previousTerm = previousState.searchTerm,
                    previousSearched = previousState.lastSearchedTerm,
                )
            ).data == true

            if (!shouldSearch) {
                Reduction.none()
            } else {
                Reduction(
                    partialStateChange = TrackListLoading(search = true),
                    derivedIntents = listOf(PerformSearch(intent.term))
                )
            }
        }

        is SongsListIntent.TypingSearchTerm -> {
            Reduction.state(SearchTermChanged(intent.term))
        }
    }
}
