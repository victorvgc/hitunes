package com.skilltest.hitunes.wear.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.FilledIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButton
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.skilltest.core.wearsync.NowPlaying
import com.skilltest.core.wearsync.WearCommand
import com.skilltest.design.component.ThumbnailImage
import com.skilltest.hitunes.wear.R
import com.skilltest.design.R as DesignR

internal const val ARTWORK_PLACEHOLDER_TAG = "artwork-placeholder"

private val ArtworkSize = 52.dp

// With nothing playing the same layout stays on screen: a placeholder colour stands in for
// the artwork and every control is disabled, so the watch never shows an empty screen.
@Composable
fun WearPlayerContent(
    nowPlaying: NowPlaying?,
    onCommand: (WearCommand) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (nowPlaying != null) {
            ThumbnailImage(
                url = nowPlaying.artworkUrl,
                contentDescription = null,
                shape = CircleShape,
                modifier = Modifier.size(ArtworkSize),
            )
        } else {
            Box(
                modifier = Modifier
                    .size(ArtworkSize)
                    .background(MaterialTheme.colorScheme.surfaceContainer, CircleShape)
                    .testTag(ARTWORK_PLACEHOLDER_TAG)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = nowPlaying?.title ?: stringResource(R.string.nothing_playing),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            // Long names scroll slowly, like on the phone.
            modifier = Modifier.basicMarquee(),
        )
        Text(
            text = nowPlaying?.artist ?: stringResource(R.string.nothing_playing_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onCommand(WearCommand.Previous) },
                enabled = nowPlaying?.hasPrevious == true,
            ) {
                Icon(
                    painter = painterResource(DesignR.drawable.ic_backward),
                    contentDescription = stringResource(R.string.acc_previous_song),
                )
            }

            val isPlaying = nowPlaying?.isPlaying == true
            FilledIconButton(
                onClick = { onCommand(if (isPlaying) WearCommand.Pause else WearCommand.Play) },
                enabled = nowPlaying != null,
            ) {
                Icon(
                    painter = painterResource(
                        if (isPlaying) DesignR.drawable.ic_pause else DesignR.drawable.ic_play
                    ),
                    contentDescription = stringResource(
                        if (isPlaying) R.string.acc_pause else R.string.acc_play
                    ),
                )
            }

            IconButton(
                onClick = { onCommand(WearCommand.Next) },
                enabled = nowPlaying?.hasNext == true,
            ) {
                Icon(
                    modifier = Modifier.rotate(180f),
                    painter = painterResource(DesignR.drawable.ic_backward),
                    contentDescription = stringResource(R.string.acc_next_song),
                )
            }
        }
    }
}

// Small round, large round and square watches.
@WearPreviewDevices
annotation class WearPlayerPreview

@WearPlayerPreview
@Composable
private fun WearPlayerPlayingPreview() {
    MaterialTheme {
        WearPlayerContent(nowPlaying = previewNowPlaying, onCommand = {})
    }
}

@WearPlayerPreview
@Composable
private fun WearPlayerPausedPreview() {
    MaterialTheme {
        WearPlayerContent(
            nowPlaying = previewNowPlaying.copy(isPlaying = false, hasPrevious = true),
            onCommand = {},
        )
    }
}

@WearPlayerPreview
@Composable
private fun WearPlayerLongTitlePreview() {
    MaterialTheme {
        WearPlayerContent(
            nowPlaying = previewNowPlaying.copy(
                title = "The Girl Is Mine (feat. Destiny's Child & Brandy)",
            ),
            onCommand = {},
        )
    }
}

@WearPlayerPreview
@Composable
private fun WearPlayerEmptyPreview() {
    MaterialTheme {
        WearPlayerContent(nowPlaying = null, onCommand = {})
    }
}

private val previewNowPlaying = NowPlaying(
    title = "Thriller",
    artist = "Michael Jackson",
    artworkUrl = "",
    isPlaying = true,
    hasNext = true,
    hasPrevious = false,
)
