package com.skilltest.internal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.skilltest.design.theme.Dimens
import com.skilltest.design.util.PreviewBox
import com.skilltest.design.util.ScreenPreview
import com.skilltest.internal.R
import com.skilltest.internal.ui.model.UiTrackAlbumInfo
import com.skilltest.internal.ui.viewmodel.AlbumComponentEffect
import com.skilltest.internal.ui.viewmodel.AlbumComponentIntent
import com.skilltest.internal.ui.viewmodel.AlbumViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumSheet(
    trackAlbumInfo: UiTrackAlbumInfo,
    onDismiss: () -> Unit,
    onNavigateToAlbumDetails: (albumId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlbumViewModel = koinViewModel { parametersOf(trackAlbumInfo.albumId) },
) {
    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is AlbumComponentEffect.NavigateToAlbumDetails -> onNavigateToAlbumDetails(effect.albumId)
                is AlbumComponentEffect.NavigateToSongDetails -> Unit
            }
        }
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        AlbumSheetContent(
            trackAlbumInfo = trackAlbumInfo,
            onViewAlbum = {
                viewModel.onIntent(
                    AlbumComponentIntent.OpenAlbumDetails(
                        albumId = trackAlbumInfo.albumId,
                        trackId = trackAlbumInfo.trackId,
                        trackName = trackAlbumInfo.trackName,
                        artistId = trackAlbumInfo.artistId,
                        artistName = trackAlbumInfo.artistName,
                    )
                )
            },
        )
    }
}

@Composable
private fun AlbumSheetContent(
    trackAlbumInfo: UiTrackAlbumInfo,
    onViewAlbum: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onBackground,
    ) {
        Column(
            modifier = modifier
                .padding(horizontal = Dimens.padding24, vertical = Dimens.padding16),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = trackAlbumInfo.trackName,
                style = MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                )
            )

            Spacer(modifier = Modifier.height(Dimens.padding8))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = trackAlbumInfo.artistName,
                style = MaterialTheme.typography.titleSmall.copy(
                    textAlign = TextAlign.Center,
                )
            )

            Spacer(modifier = Modifier.height(Dimens.padding16))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onViewAlbum),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.padding16)
            ) {
                Icon(
                    painter = painterResource(com.skilltest.design.R.drawable.ic_setlist),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.view_album),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Spacer(modifier = Modifier.height(Dimens.padding16))
        }
    }
}

@ScreenPreview
@Composable
private fun AlbumSheetContentPreview() {
    PreviewBox {
        AlbumSheetContent(
            trackAlbumInfo = UiTrackAlbumInfo(
                trackName = "Thriller",
                artistName = "Michael Jackson",
                trackId = "123",
                albumId = "123",
                artistId = "456",
            ),
            onViewAlbum = {},
        )
    }
}
