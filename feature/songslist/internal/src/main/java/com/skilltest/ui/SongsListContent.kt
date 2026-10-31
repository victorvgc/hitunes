package com.skilltest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.skilltest.core.utils.ScreenPreview
import com.skilltest.design.theme.Dimens
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.ui.model.UiTrack
import com.skilltest.ui.viewmodel.SongsListIntent
import com.skilltest.ui.viewmodel.SongsListState

@Composable
fun SongsListContent(
    state: SongsListState,
    onIntent: (SongsListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(
                    horizontal = Dimens.padding24,
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimens.padding16)
        ) {
            SongsListHeader(
                state = state,
                onIntent = onIntent
            )

            if ((state.isSearching.not() || state.searchTerm.isEmpty()) &&
                (state.recentlyPlayed.isNotEmpty() || state.isLoadingRecentlyPlayed) || state.isLoadingSearch.not()
            ) {
                Text(
                    text = stringResource(R.string.recently_played),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                )
            }

            SongListComponent(
                isSearching = state.isSearching && state.searchTerm.isNotEmpty(),
                isLoading = state.isLoadingRecentlyPlayed || state.isLoadingSearch,
                recentlyPlayed = state.recentlyPlayed,
                searchResults = state.searchResult,
                onTrackClick = {
                    onIntent(SongsListIntent.OpenTrackDetails(it))
                },
                onTrackMoreActionsClick = {
                    onIntent(SongsListIntent.OpenTrackMoreOptions(it))
                }
            )
        }
    }
}

@ScreenPreview
@Composable
private fun SongsListContentPreview(
    @PreviewParameter(SongsListContentParamProvider::class)
    state: SongsListState,
) {
    HiTunesTheme() {
        SongsListContent(state, onIntent = {})
    }
}

private class SongsListContentParamProvider : PreviewParameterProvider<SongsListState> {
    override val values: Sequence<SongsListState> = sequenceOf(
        SongsListState(),
        SongsListState(
            isSearching = true
        ),
        SongsListState(
            isSearching = true,
            searchTerm = "Michael Jackson"
        ),
        SongsListState(
            isSearching = true,
            searchTerm = "Michael Jackson",
            searchResult = buildList {
                for (i in 0..10) {
                    add(
                        UiTrack(
                            id = i.toString(),
                            name = "Song $i",
                            artistName = "Artist $i",
                            artwork = "",
                            isExplicit = i % 2 == 0
                        )
                    )
                }
            }
        ),
        SongsListState(
            recentlyPlayed = buildList {
                for (i in 0..10) {
                    add(
                        UiTrack(
                            id = i.toString(),
                            name = "Song $i",
                            artistName = "Artist $i",
                            artwork = "",
                            isExplicit = i % 2 == 0
                        )
                    )
                }
            }
        ),
        SongsListState(
            isLoadingSearch = true
        ),
        SongsListState(
            isLoadingRecentlyPlayed = true
        ),
    )

}
