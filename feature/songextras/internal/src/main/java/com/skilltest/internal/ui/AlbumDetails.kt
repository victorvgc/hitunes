package com.skilltest.internal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.skilltest.design.component.DetailsTopBar
import com.skilltest.design.component.ThumbnailImage
import com.skilltest.design.screen.LoadingScreen
import com.skilltest.design.theme.Dimens
import com.skilltest.design.util.PreviewBox
import com.skilltest.design.util.ScreenPreview
import com.skilltest.domain.model.Track
import com.skilltest.internal.ui.model.UiAlbum
import com.skilltest.internal.ui.viewmodel.AlbumComponentEffect
import com.skilltest.internal.ui.viewmodel.AlbumComponentIntent
import com.skilltest.internal.ui.viewmodel.AlbumViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

@Composable
fun AlbumDetailsScreen(
    albumId: String,
    onBack: () -> Unit = {},
    onTrackSelected: (Track, List<Track>) -> Unit = { _, _ -> },
    viewModel: AlbumViewModel = koinViewModel { parametersOf(albumId) }
) {
    Timber.d("VICTOR: $albumId")

    val state = viewModel.state.collectAsState().value
    val currentOnTrackSelected by rememberUpdatedState(onTrackSelected)

    LaunchedEffect(albumId) {
        viewModel.onIntent(AlbumComponentIntent.LoadingDetails(albumId))
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            if (effect is AlbumComponentEffect.NavigateToSongDetails) {
                currentOnTrackSelected(
                    effect.track,
                    viewModel.state.value.album.trackList.map { it.toDomain() },
                )
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        DetailsTopBar(
            title = state.album.albumName.ifBlank { "Album" },
            onBack = onBack,
        )
        if (state.isLoading) {
            Box(modifier = Modifier.weight(1f)) { LoadingScreen() }
        } else {
            AlbumDetailsContent(
                album = state.album,
                onIntent = viewModel::onIntent,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun AlbumDetailsContent(
    album: UiAlbum,
    onIntent: (AlbumComponentIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Timber.d("VICTOR: ${album.albumName}")
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.padding24)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ThumbnailImage(
                modifier = Modifier.size(Dimens.size120),
                url = album.albumCoverUrl,
                contentDescription = album.albumName
            )
        }

        Spacer(modifier = Modifier.height(Dimens.padding16))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = album.albumName,
            style = MaterialTheme.typography.titleLarge.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
        )

        Spacer(modifier = Modifier.height(Dimens.padding8))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = album.artistName,
            style = MaterialTheme.typography.titleSmall.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
        )

        Spacer(modifier = Modifier.height(Dimens.padding48))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(Dimens.padding16),
        ) {
            items(album.trackList) { track ->
                TrackItem(track) {
                    onIntent(AlbumComponentIntent.OnClickTrack(track))
                }
            }
        }
    }
}

@Composable
private fun TrackItem(
    track: UiAlbum.UiTrack,
    onClick: () -> Unit,
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
                text = track.trackName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = track.artistName,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.tertiary
                )
            )
        }
    }
}

@ScreenPreview
@Composable
private fun AlbumDetailsPreview() {
    PreviewBox(modifier = Modifier
        .width(Dimens.size411)
        .height(Dimens.size891)) {
        Column(modifier = Modifier.fillMaxSize()) {
            DetailsTopBar(title = "Thriller", onBack = {})
            AlbumDetailsContent(
                album = UiAlbum(
                    albumName = "Thriller",
                    albumCoverUrl = "",
                    artistName = "Michael Jackson",
                    trackList = buildList {
                        for (i in 1..10) {
                            add(UiAlbum.UiTrack(i.toString(), "Track $i", "Michael Jackson", ""))
                        }
                    }
                ),
                onIntent = {},
                modifier = Modifier.weight(1f),
            )
        }
    }
}
