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
import com.skilltest.design.util.ScreenPreview
import com.skilltest.design.theme.Dimens
import com.skilltest.design.util.PreviewBox
import com.skilltest.ui.model.UiTrack
import com.skilltest.ui.viewmodel.SongsListIntent
import com.skilltest.ui.viewmodel.SongsListState

@Composable
fun SongsListContent(
    state: SongsListState,
    onIntent: (SongsListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showRecentlyPlayed = !state.isSearching || state.searchTerm.isEmpty()

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

            if (showRecentlyPlayed) {
                Text(
                    text = stringResource(R.string.recently_played),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                )
            }

            SongListComponent(
                isSearching = !showRecentlyPlayed,
                isLoading = if (showRecentlyPlayed) state.isLoadingRecentlyPlayed else state.isLoadingSearch,
                recentlyPlayed = state.recentlyPlayed,
                searchResults = if (state.searchTerm.length >= 3) state.searchResult else emptyList(),
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
    PreviewBox {
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
