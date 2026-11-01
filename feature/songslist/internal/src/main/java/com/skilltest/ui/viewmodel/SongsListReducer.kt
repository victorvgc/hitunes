package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseReducer
import com.skilltest.core.presentation.viewmodel.Reduction
import com.skilltest.domain.model.Track
import com.skilltest.domain.usecase.SearchSongsByTermUseCase
import com.skilltest.domain.usecase.ShouldShowSearchLoadingUseCase
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

class SongsListReducer(
    private val searchSongsByTermUseCase: SearchSongsByTermUseCase,
    private val shouldShowSearchLoadingUseCase: ShouldShowSearchLoadingUseCase,
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
                derivedIntents = listOf(SongsListIntent.LoadingRecentlyPlayed)
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
            Reduction.effect(NavigateToTrackDetails(intent.trackId))
        }

        is SongsListIntent.OpenTrackMoreOptions -> {
            Reduction.effect(NavigateToTrackOptions(intent.track))
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
                                    isExplicit = i % 2 == 0,
                                    albumName = "Album $i",
                                    albumId = "album-$i",
                                    artistId = "artist-$i",
                                )
                            )
                        }
                    }
                )
            )
        }

        is SongsListIntent.StartPerformSearch -> {
            val shouldSearch = shouldShowSearchLoadingUseCase(
                ShouldShowSearchLoadingUseCase.Request(
                    intent.term
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
            val shouldSearch = shouldShowSearchLoadingUseCase(
                ShouldShowSearchLoadingUseCase.Request(
                    intent.term
                )
            ).data == true

            Reduction.state(
                TrackListLoading(search = shouldSearch)
            )
        }
    }
}
