package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseReducer
import com.skilltest.core.presentation.viewmodel.Reduction
import com.skilltest.domain.model.Track
import com.skilltest.ui.viewmodel.SongsListEffect.NavigateToTrackDetails
import com.skilltest.ui.viewmodel.SongsListEffect.NavigateToTrackOptions
import com.skilltest.ui.viewmodel.SongsListIntent.PerformSearch
import com.skilltest.ui.viewmodel.SongsListStateChange.LoadedRecentlyPlayed
import com.skilltest.ui.viewmodel.SongsListStateChange.LoadedSearchResults
import com.skilltest.ui.viewmodel.SongsListStateChange.SearchCleared
import com.skilltest.ui.viewmodel.SongsListStateChange.StartSearching
import com.skilltest.ui.viewmodel.SongsListStateChange.TrackListLoading
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class SongsListReducer : BaseReducer<SongsListState, SongsListIntent, SongsListEffect> {
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
                derivedIntents = listOf(SongsListIntent.LoadingRecentlyPlayed)
            )
        }

        is PerformSearch -> {
            delay(2.seconds) // todo: use case call

            Reduction.state(
                LoadedSearchResults(
                    intent.term,
                    buildList {
                        for (i in 1..10) {
                            add(
                                Track(
                                    id = i.toString(),
                                    name = "Searched Song $i",
                                    artistName = "Artist $i",
                                    artworkUrl = "https://www.diamondpet.com/wp-content/uploads/2021/03/kitten-sitting-on-floor-031621.jpg",
                                    isExplicit = i % 2 == 0
                                )
                            )
                        }
                    }
                )
            )
        }

        is SongsListIntent.OpenTrackDetails -> {
            Reduction.effect(NavigateToTrackDetails(intent.trackId))
        }

        is SongsListIntent.OpenTrackMoreOptions -> {
            Reduction.effect(NavigateToTrackOptions(intent.trackId))
        }

        SongsListIntent.SearchClick -> {
            Reduction.state(
                StartSearching
            )
        }

        SongsListIntent.LoadingRecentlyPlayed -> {
            delay(2.seconds) // todo: use case call

            Reduction.state(
                LoadedRecentlyPlayed(
                    buildList {
                        for (i in 1..10) {
                            add(
                                Track(
                                    id = i.toString(),
                                    name = "Song $i",
                                    artistName = "Artist $i",
                                    artworkUrl = "https://www.diamondpet.com/wp-content/uploads/2021/03/kitten-sitting-on-floor-031621.jpg",
                                    isExplicit = i % 2 == 0
                                )
                            )
                        }
                    }
                )
            )
        }

        is SongsListIntent.StartPerformSearch -> {
            if (intent.term.length < 3) {
                // todo: rule will be on a real use case later
                Reduction.none()
            } else {
                Reduction(
                    partialStateChange = TrackListLoading(search = true),
                    derivedIntents = listOf(PerformSearch(intent.term))
                )
            }
        }

        SongsListIntent.TypingSearchTerm -> {
            Reduction.state(
                TrackListLoading(search = true)
            )
        }
    }
}
