package com.skilltest.internal

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.github.takahirom.roborazzi.captureScreenRoboImage
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.internal.ui.AlbumDetails
import com.skilltest.internal.ui.AlbumSheet
import com.skilltest.internal.ui.model.UiTrackAlbumInfo
import com.skilltest.internal.ui.viewmodel.AlbumReducer
import com.skilltest.internal.ui.viewmodel.AlbumViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import androidx.test.ext.junit.runners.AndroidJUnit4

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w411dp-h891dp")
@OptIn(ExperimentalRoborazziApi::class)
class AlbumScreenshotTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun albumSheet() {
        composeRule.setContent {
            HiTunesTheme(darkTheme = false) {
                AlbumSheet(
                    trackAlbumInfo = UiTrackAlbumInfo(
                        trackId = "track-1",
                        albumId = "album-2",
                        trackName = "Thriller",
                        artistId = "artist-3",
                        artistName = "Michael Jackson",
                    ),
                    onDismiss = {},
                    onNavigateToAlbumDetails = {},
                    viewModel = AlbumViewModel(AlbumReducer()),
                )
            }
        }
        composeRule.waitForIdle()
        captureScreenRoboImage()
    }

    @Test
    fun albumDetails() {
        composeRule.setContent {
            HiTunesTheme(darkTheme = false) {
                AlbumDetails(
                    albumId = "album-2",
                    trackId = "track-1",
                    trackName = "Thriller",
                    artistId = "artist-3",
                    artistName = "Michael Jackson",
                )
            }
        }
        composeRule.waitForIdle()
        captureScreenRoboImage()
    }
}
