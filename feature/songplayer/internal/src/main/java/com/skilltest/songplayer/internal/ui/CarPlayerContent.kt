package com.skilltest.songplayer.internal.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.skilltest.design.R
import com.skilltest.design.component.DetailsTopBar
import com.skilltest.design.component.ThumbnailImage
import com.skilltest.design.theme.Dimens
import com.skilltest.design.util.AutoPreview
import com.skilltest.design.util.PreviewBox
import com.skilltest.design.util.sharedImageTransition
import com.skilltest.domain.model.Track
import com.skilltest.songplayer.internal.viewmodel.PlayerIntent
import com.skilltest.songplayer.internal.viewmodel.PlayerState

// Song page for car screens: wide and short, so the cover sits next to the song info
// and the seek bar and controls run along the bottom.
@Composable
internal fun CarSongDetailsContent(
    track: Track,
    state: PlayerState,
    onIntent: (PlayerIntent) -> Unit,
    onBack: (() -> Unit)?,
    onAlbum: () -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    Column(modifier = modifier.fillMaxSize()) {
        DetailsTopBar(
            title = track.name,
            onBack = onBack,
            actions = { AlbumButton(onClick = onAlbum) },
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = Dimens.padding48),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.padding48),
            ) {
                ThumbnailImage(
                    url = track.artworkUrl,
                    contentDescription = track.name,
                    modifier = Modifier.sharedImageTransition(
                        size = Dimens.size246,
                        trackId = track.id,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = track.name,
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight(600)
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                    )
                    Spacer(modifier = Modifier.height(Dimens.padding8))
                    Text(
                        text = track.artistName,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight(500)
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.padding48))

            PlayerSeekBar(
                state,
                onIntent,
                thumbSize = Dimens.size36,
                trackThickness = Dimens.size12
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.padding8, bottom = Dimens.padding24),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                RepeatAction(state, onIntent, iconSize = Dimens.size48)
                PreviousAction(state, onIntent, iconSize = Dimens.size72)
                PlayPauseAction(state, onIntent, size = Dimens.size96)
                NextAction(state, onIntent, iconSize = Dimens.size72)
                Box { } // empty box to balance the layout
            }
        }
    }
}

@Composable
private fun AlbumButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier.clickable {
            onClick()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_album_auto),
            contentDescription = null,
            modifier = Modifier.size(Dimens.size40),
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(Dimens.padding8))
        Text(
            text = stringResource(com.skilltest.songplayer.internal.R.string.album),
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@AutoPreview
@Composable
private fun CarSongDetailsContentPreview() {
    PreviewBox(modifier = Modifier.fillMaxSize()) {
        CarSongDetailsContent(
            track = previewTrack,
            state = PlayerState(
                track = previewTrack,
                isPlaying = true,
                positionMs = 83_000,
                durationMs = 357_000,
                hasNext = true,
                hasPrevious = true,
            ),
            onIntent = {},
            onBack = {},
            onAlbum = {},
        )
    }
}
