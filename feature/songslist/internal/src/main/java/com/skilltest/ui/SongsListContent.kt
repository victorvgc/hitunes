package com.skilltest.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.skilltest.core.utils.ScreenPreview
import com.skilltest.design.component.ActionButton
import com.skilltest.design.component.ActionButtonDefaults
import com.skilltest.design.component.ThumbnailImage
import com.skilltest.design.theme.Dimens
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.ui.model.UiTrack
import com.skilltest.ui.viewmodel.SongsListIntent
import com.skilltest.ui.viewmodel.SongsListState
import com.skilltest.design.R as designR

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.padding20),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.songs_list_title),
                    style = MaterialTheme.typography.displaySmall
                )

                if (state.isSearching.not()) {
                    ActionButton(
                        colors = ActionButtonDefaults.colors().copy(
                            container = Color.Transparent
                        ),
                        content = {
                            Icon(
                                painter = painterResource(designR.drawable.ic_search),
                                contentDescription = stringResource(R.string.acc_search_a_song)
                            )
                        },
                        onClick = {
                            onIntent(SongsListIntent.SearchClick)
                        }
                    )
                }
            }

            if (state.isSearching) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.searchTerm,
                    onValueChange = { input ->
                        onIntent(SongsListIntent.LoadSearchResults(input))
                    },
                    maxLines = 1,
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    colors = TextFieldDefaults.colors().copy(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(designR.drawable.ic_search),
                            contentDescription = null
                        )
                    },
                    trailingIcon = if (state.searchTerm.isEmpty()) null else {
                        {
                            ActionButton(
                                content = {
                                    Icon(
                                        painter = painterResource(designR.drawable.ic_close),
                                        contentDescription = stringResource(R.string.acc_search_a_song),
                                        modifier = Modifier.size(Dimens.size24),
                                    )
                                },
                                onClick = {
                                    onIntent(SongsListIntent.ClearSearch)
                                }
                            )
                        }
                    }
                )
            }

            if (state.isSearching.not() && state.recentlyPlayed.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.recently_played),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimens.padding16)
            ) {
                if (state.isSearching) {
                    items(state.searchResult) { track ->
                        TrackItem(
                            track = track,
                            onClick = {
                                onIntent(SongsListIntent.OpenDetailsForTrack(track.id))
                            },
                            onMoreClick = {
                                onIntent(SongsListIntent.OpenMoreOptionsForTrack(track.id))
                            }
                        )
                    }
                } else {
                    items(state.recentlyPlayed) { track ->
                        TrackItem(
                            track = track,
                            onClick = {
                                onIntent(SongsListIntent.OpenDetailsForTrack(track.id))
                            },
                            onMoreClick = {
                                onIntent(SongsListIntent.OpenMoreOptionsForTrack(track.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrackItem(
    track: UiTrack,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.spacedBy(Dimens.padding16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ThumbnailImage(
            modifier = Modifier.size(Dimens.size52),
            url = track.artwork,
            contentDescription = null
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens.padding4)
        ) {
            Text(
                text = track.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = track.artistName,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.tertiary
                )
            )
        }

        ActionButton(
            colors = ActionButtonDefaults.colors().copy(
                content = MaterialTheme.colorScheme.tertiary
            ),
            content = {
                Icon(
                    painter = painterResource(designR.drawable.ic_more_actions),
                    contentDescription = stringResource(R.string.acc_more_actions),
                )
            },
            onClick = onMoreClick
        )
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
    )

}
