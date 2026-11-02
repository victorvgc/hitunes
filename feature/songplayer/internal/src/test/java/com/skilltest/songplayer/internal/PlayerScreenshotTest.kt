package com.skilltest.songplayer.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.domain.model.Track
import com.skilltest.songplayer.internal.ui.MiniPlayerContent
import com.skilltest.songplayer.internal.ui.SongDetailsContent
import com.skilltest.songplayer.internal.viewmodel.PlayerState
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h891dp")
class PlayerScreenshotTest {
    @Test
    fun miniPlayer() {
        captureRoboImage {
            HiTunesTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.width(411.dp)
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    MiniPlayerContent(
                        state = PlayerState(
                            track = track,
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
        }
    }

    @Test
    fun songDetails() {
        captureRoboImage {
            HiTunesTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.width(411.dp)
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    SongDetailsContent(
                        track = track,
                        state = PlayerState(
                            track = track,
                            positionMs = 83_000,
                            durationMs = 357_000,
                            hasNext = true,
                            hasPrevious = true,
                        ),
                        onIntent = {},
                    )
                }
            }
        }
    }

    private val track = Track(
        id = "track-1",
        name = "Thriller",
        artistName = "Michael Jackson",
        albumName = "Thriller",
        artworkUrl = "",
        isExplicit = false,
    )
}
