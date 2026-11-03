package com.skilltest.songplayer.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import com.skilltest.design.theme.Dimens
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.domain.model.Track
import com.skilltest.songplayer.internal.ui.CarSongDetailsContent
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
                    modifier = Modifier
                        .width(Dimens.size411)
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
                    modifier = Modifier
                        .width(Dimens.size411)
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

    // Right half of a tablet in landscape (1280x800): no top bar, menu on the cover row.
    @Test
    @Config(qualifiers = "w1280dp-h800dp")
    fun songDetailsTabletLandscapePane() {
        captureSongDetailsPane(width = 640.dp, height = 800.dp)
    }

    // Car screens get their own song page: cover next to the info, one row of controls.
    @Test
    @Config(qualifiers = "w1024dp-h768dp")
    fun carSongDetails() {
        captureRoboImage {
            HiTunesTheme(darkTheme = false) {
                Box(
                    modifier = Modifier
                        .size(1024.dp, 768.dp)
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    CarSongDetailsContent(
                        track = track,
                        state = PlayerState(
                            track = track,
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
        }
    }

    private fun captureSongDetailsPane(width: Dp, height: Dp) {
        captureRoboImage {
            HiTunesTheme(darkTheme = false) {
                Box(
                    modifier = Modifier
                        .size(width, height)
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
                        onMore = {},
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
