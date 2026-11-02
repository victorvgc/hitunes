package com.skilltest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.ui.model.UiTrack
import com.skilltest.ui.viewmodel.SongsListState
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h891dp")
class SongsListScreenshotTest {
    @Test
    fun emptySongsList() {
        captureRoboImage {
            HiTunesTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.width(411.dp)
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    SongsListContent(state = SongsListState(), onIntent = {})
                }
            }
        }
    }

    @Test
    fun songRowWithMoreActions() {
        captureRoboImage {
            HiTunesTheme(darkTheme = false) {
                Column(
                    modifier = Modifier.width(411.dp)
                        .background(MaterialTheme.colorScheme.background),
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
        }
    }
}
