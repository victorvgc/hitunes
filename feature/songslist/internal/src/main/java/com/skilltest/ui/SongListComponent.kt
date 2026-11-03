package com.skilltest.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.skilltest.design.component.ActionButton
import com.skilltest.design.component.ActionButtonDefaults
import com.skilltest.design.component.SkeletonBox
import com.skilltest.design.component.ThumbnailImage
import com.skilltest.design.theme.Dimens
import com.skilltest.design.util.ComponentPreview
import com.skilltest.design.util.PreviewBox
import com.skilltest.ui.model.UiTrack
import com.skilltest.design.R as designR

private const val SKELETON_LOADING_SIZE = 10

@Composable
internal fun SongListComponent(
    isSearching: Boolean,
    isLoading: Boolean,
    recentlyPlayed: List<UiTrack>,
    searchResults: List<UiTrack>,
    onTrackClick: (UiTrack) -> Unit,
    onTrackMoreActionsClick: (UiTrack) -> Unit,
) {
    val trackList = if (isSearching) {
        searchResults
    } else {
        recentlyPlayed
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.padding16)
    ) {
        if (isLoading) {
            items(SKELETON_LOADING_SIZE) {
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.size52)
                )
            }
        } else {
            items(trackList) { track ->
                TrackItem(
                    track = track,
                    onClick = {
                        onTrackClick(track)
                    },
                    onMoreClick = {
                        onTrackMoreActionsClick(track)
                    }
                )
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
                    contentDescription = stringResource(com.skilltest.ui.R.string.acc_more_actions),
                )
            },
            onClick = onMoreClick
        )
    }
}

@ComponentPreview
@Composable
private fun SongListComponentPreview() {
    PreviewBox(modifier = Modifier
        .width(Dimens.size320)
        .height(Dimens.size80)
    ) {
        SongListComponent(
            isSearching = false,
            isLoading = false,
            recentlyPlayed = listOf(
                UiTrack(
                    id = "track-1",
                    name = "Thriller",
                    artistName = "Michael Jackson",
                    artwork = "",
                    isExplicit = false,
                    artistId = "artist-3",
                    albumId = "album-2",
                )
            ),
            searchResults = emptyList(),
            onTrackClick = {},
            onTrackMoreActionsClick = {},
        )
    }
}
