package com.skilltest.songplayer.internal.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.skilltest.design.component.ThumbnailImage
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
) {
    val track = state.track ?: return
    Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpenSong)) {
                Text(track.name, style = MaterialTheme.typography.titleSmall)
                Text(track.artistName, style = MaterialTheme.typography.bodySmall)
            }
            PlaybackActions(state, onIntent)
            PlayerSeekBar(state, onIntent)
        }
    }
}

@Composable
internal fun SongDetailsContent(
    track: Track,
    state: PlayerState,
    onIntent: (PlayerIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ThumbnailImage(
            url = track.artworkUrl,
            contentDescription = track.name,
        )
        Text(track.name, style = MaterialTheme.typography.headlineMedium)
        Text(track.artistName, style = MaterialTheme.typography.titleMedium)
        if (track.albumName.isNotBlank()) Text(track.albumName)
        PlaybackActions(state, onIntent)
        PlayerSeekBar(state, onIntent)
    }
}

@Composable
private fun PlaybackActions(
    state: PlayerState,
    onIntent: (PlayerIntent) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        IconButton(
            onClick = { onIntent(PlayerIntent.Previous) },
            enabled = state.hasPrevious,
            modifier = Modifier.semantics { contentDescription = "Previous track" },
        ) {
            Text("⏮")
        }
        IconButton(
            onClick = { onIntent(PlayerIntent.Backward) },
            enabled = state.track != null,
            modifier = Modifier.semantics { contentDescription = "Back 10 seconds" },
        ) {
            Text("-10s")
        }
        IconButton(
            onClick = { onIntent(if (state.isPlaying) PlayerIntent.Pause else PlayerIntent.Play) },
            enabled = state.track != null,
            modifier = Modifier.semantics {
                contentDescription = if (state.isPlaying) "Pause" else "Play"
            },
        ) {
            Text(if (state.isPlaying) "❚❚" else "▶")
        }
        IconButton(
            onClick = { onIntent(PlayerIntent.Forward) },
            enabled = state.track != null,
            modifier = Modifier.semantics { contentDescription = "Forward 10 seconds" },
        ) {
            Text("+10s")
        }
        IconButton(
            onClick = { onIntent(PlayerIntent.Next) },
            enabled = state.hasNext,
            modifier = Modifier.semantics { contentDescription = "Next track" },
        ) {
            Text("⏭")
        }
    }
}

@Composable
private fun PlayerSeekBar(state: PlayerState, onIntent: (PlayerIntent) -> Unit) {
    var dragging by remember { mutableStateOf<Float?>(null) }
    val progress = if (state.durationMs > 0) {
        (state.positionMs.toFloat() / state.durationMs).coerceIn(0f, 1f)
    } else 0f
    Slider(
        value = dragging ?: progress,
        onValueChange = { dragging = it },
        onValueChangeFinished = {
            dragging?.let { onIntent(PlayerIntent.SeekTo((it * state.durationMs).toLong())) }
            dragging = null
        },
        enabled = state.durationMs > 0,
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
    PreviewBox {
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
    PreviewBox {
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
        )
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
