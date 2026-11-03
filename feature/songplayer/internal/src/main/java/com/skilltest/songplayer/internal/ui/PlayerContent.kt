package com.skilltest.songplayer.internal.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.skilltest.design.R
import com.skilltest.design.component.ActionButton
import com.skilltest.design.component.ActionButtonDefaults
import com.skilltest.design.component.DetailsTopBar
import com.skilltest.design.component.ThumbnailImage
import com.skilltest.design.theme.Dimens
import com.skilltest.design.util.ComponentPreview
import com.skilltest.design.util.PreviewBox
import com.skilltest.design.util.ScreenPreview
import com.skilltest.domain.model.Track
import com.skilltest.songplayer.internal.viewmodel.PlayerIntent
import com.skilltest.songplayer.internal.viewmodel.PlayerState

@Composable
internal fun MiniPlayerContent(
    state: PlayerState,
    onIntent: (PlayerIntent) -> Unit,
    onOpenSong: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val track = state.track ?: return
    Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val progress = if (state.durationMs > 0) {
                (state.positionMs.toFloat() / state.durationMs).coerceIn(0f, 1f)
            } else 0f
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.size4)
                    .background(
                        MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(Dimens.radius8)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .height(Dimens.size4)
                        .background(
                            MaterialTheme.colorScheme.onSurface,
                            RoundedCornerShape(Dimens.radius8)
                        )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Dimens.padding16,
                        end = Dimens.padding16,
                        bottom = Dimens.padding16,
                        top = Dimens.padding8
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.padding16),
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onOpenSong),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.padding16),
                ) {
                    ThumbnailImage(
                        url = track.artworkUrl,
                        contentDescription = null,
                        modifier = Modifier.artworkModifier(
                            size = Dimens.size52,
                            trackId = track.id,
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = track.name,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = track.artistName,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                PlayPauseAction(state, onIntent)
            }
        }
    }
}

@Composable
internal fun SongDetailsContent(
    track: Track,
    state: PlayerState,
    onIntent: (PlayerIntent) -> Unit,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.padding24),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            ThumbnailImage(
                url = track.artworkUrl,
                contentDescription = track.name,
                modifier = Modifier.artworkModifier(
                    size = Dimens.size246,
                    trackId = track.id,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                ),
            )
        }

        Column(
            modifier = Modifier
                .padding(vertical = Dimens.padding48)
                .weight(1f)
        ) {
            Text(
                track.name, style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(Dimens.padding8))
            Text(
                track.artistName, style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(Dimens.padding24))

            PlayerSeekBar(state, onIntent)
            Spacer(modifier = Modifier.height(Dimens.padding16))
            PlaybackActions(state, onIntent)
        }
    }
}

@Composable
private fun Modifier.artworkModifier(
    size: Dp,
    trackId: String,
    sharedTransitionScope: SharedTransitionScope?,
    animatedVisibilityScope: AnimatedVisibilityScope?,
): Modifier {
    if (sharedTransitionScope == null || animatedVisibilityScope == null) {
        return this.size(size)
    }
    return with(sharedTransitionScope) {
        Modifier
            .sharedElement(
                rememberSharedContentState(key = "player-artwork-$trackId"),
                animatedVisibilityScope = animatedVisibilityScope,
            )
            .size(size)
    }
}

@Composable
private fun PlaybackActions(
    state: PlayerState,
    onIntent: (PlayerIntent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.padding16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayPauseAction(state, onIntent, size = Dimens.size72)

            val accPreviousTrack =
                stringResource(com.skilltest.songplayer.internal.R.string.acc_previous_song)

            IconButton(
                onClick = { onIntent(PlayerIntent.Previous) },
                enabled = state.hasPrevious,
                modifier = Modifier.semantics { contentDescription = accPreviousTrack },
            ) {
                Icon(
                    modifier = Modifier.size(Dimens.size36),
                    painter = painterResource(R.drawable.ic_backward),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            val accNextTrack =
                stringResource(com.skilltest.songplayer.internal.R.string.acc_next_song)

            IconButton(
                onClick = { onIntent(PlayerIntent.Next) },
                enabled = state.hasNext,
                modifier = Modifier.semantics { contentDescription = accNextTrack },
            ) {
                Icon(
                    modifier = Modifier
                        .size(Dimens.size36)
                        .rotate(180f),
                    painter = painterResource(R.drawable.ic_backward),
                    contentDescription = stringResource(com.skilltest.songplayer.internal.R.string.acc_next_song),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        val accRepeat =
            stringResource(com.skilltest.songplayer.internal.R.string.acc_play_on_repeat)

        IconButton(
            onClick = { onIntent(PlayerIntent.ToggleOnRepeat) },
            enabled = state.track != null,
            modifier = Modifier.semantics { contentDescription = accRepeat },
        ) {
            Icon(
                modifier = Modifier
                    .size(Dimens.size36)
                    .rotate(180f),
                painter = painterResource(R.drawable.ic_play_on_repeat),
                contentDescription = stringResource(com.skilltest.songplayer.internal.R.string.acc_next_song),
                tint = if (state.isRepeating) MaterialTheme.colorScheme.tertiary
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun PlayPauseAction(
    state: PlayerState,
    onIntent: (PlayerIntent) -> Unit,
    size: Dp = Dimens.size48
) {
    val accessibilityText = if (state.isPlaying) {
        stringResource(com.skilltest.songplayer.internal.R.string.acc_pause)
    } else {
        stringResource(com.skilltest.songplayer.internal.R.string.acc_play)
    }

    ActionButton(
        onClick = { onIntent(if (state.isPlaying) PlayerIntent.Pause else PlayerIntent.Play) },
        enabled = state.track != null,
        modifier = Modifier.semantics { contentDescription = accessibilityText },
        colors = ActionButtonDefaults.colors().copy(
            content = MaterialTheme.colorScheme.surface,
            container = MaterialTheme.colorScheme.tertiary,
        ),
        shape = CircleShape,
        content = {
            Icon(
                modifier = Modifier
                    .size(size)
                    .padding(Dimens.padding4),
                painter = painterResource(if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                contentDescription = null,
            )
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerSeekBar(state: PlayerState, onIntent: (PlayerIntent) -> Unit) {
    var dragging by remember { mutableStateOf<Float?>(null) }
    val progress = if (state.durationMs > 0) {
        (state.positionMs.toFloat() / state.durationMs).coerceIn(0f, 1f)
    } else 0f

    val sliderState = rememberSliderState(progress)
    LaunchedEffect(progress, dragging) {
        if (dragging == null) sliderState.value = progress
    }

    Slider(
        state = sliderState,
        onValueChange = {
            dragging = it
            sliderState.value = it
        },
        onValueChangeFinished = {
            dragging?.let { onIntent(PlayerIntent.SeekTo((it * state.durationMs).toLong())) }
            dragging = null
        },
        enabled = state.durationMs > 0,
        thumb = {
            Box(
                modifier = Modifier
                    .size(Dimens.size16)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    )
            )
        },
        track = { sliderState ->
            val range = sliderState.trackRange
            val fraction = ((sliderState.value - range.start) /
                    (range.endInclusive - range.start)).coerceIn(0f, 1f)

            Box(
                Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(fraction)
                        .align(Alignment.CenterStart)
                        .height(Dimens.size4)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(
                                topStart = Dimens.radius8,
                                bottomStart = Dimens.radius8
                            )
                        )
                )

                Box(
                    Modifier
                        .fillMaxWidth(1f - fraction)
                        .align(Alignment.CenterEnd)
                        .height(Dimens.size4)
                        .background(
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(
                                topEnd = Dimens.radius8,
                                bottomEnd = Dimens.radius8
                            )
                        )
                )
            }
        }
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(formatTime(state.positionMs), style = MaterialTheme.typography.labelSmall)
        Text(formatTime(state.durationMs), style = MaterialTheme.typography.labelSmall)
    }
}

private fun formatTime(milliseconds: Long): String {
    val seconds = milliseconds.coerceAtLeast(0) / 1_000
    return "%d:%02d".format(seconds / 60, seconds % 60)
}

@ComponentPreview
@Composable
private fun MiniPlayerContentPreview() {
    PreviewBox(modifier = Modifier.width(Dimens.size360)) {
        MiniPlayerContent(
            state = PlayerState(
                track = previewTrack,
                isPlaying = true,
                positionMs = 83_000,
                durationMs = 357_000,
                hasNext = true,
            ),
            onIntent = {},
            onOpenSong = {},
        )
    }
}

@ScreenPreview
@Composable
private fun SongDetailsContentPreview() {
    PreviewBox(
        modifier = Modifier
            .width(Dimens.size411)
            .height(Dimens.size891)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DetailsTopBar(title = previewTrack.name, onBack = {}, onMore = {})
            SongDetailsContent(
                track = previewTrack,
                state = PlayerState(
                    track = previewTrack,
                    positionMs = 83_000,
                    durationMs = 357_000,
                    hasNext = true,
                    hasPrevious = true,
                ),
                onIntent = {},
                modifier = Modifier.weight(1f),
            )
        }
    }
}

private val previewTrack = Track(
    id = "track-1",
    name = "Thriller",
    artistName = "Michael Jackson",
    albumName = "Thriller",
    artworkUrl = "",
    isExplicit = false,
)
