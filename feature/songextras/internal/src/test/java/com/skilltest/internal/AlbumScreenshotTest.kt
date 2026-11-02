package com.skilltest.internal

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureScreenRoboImage
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.domain.model.Album
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.Track
import com.skilltest.domain.repository.AlbumRepository
import com.skilltest.domain.usecase.LoadAlbumDetailsUseCase
import com.skilltest.internal.ui.AlbumDetailsScreen
import com.skilltest.internal.ui.AlbumSheet
import com.skilltest.internal.ui.model.UiTrackAlbumInfo
import com.skilltest.internal.ui.viewmodel.AlbumReducer
import com.skilltest.internal.ui.viewmodel.AlbumViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

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
                    viewModel = AlbumViewModel(
                        AlbumReducer(LoadAlbumDetailsUseCase(object : AlbumRepository {
                            override suspend fun loadAlbum(albumId: String): ResultWrapper<Album> =
                                ResultWrapper.Success(Album("", "", "", false, "", emptyList()))
                        }))
                    ),
                )
            }
        }
        composeRule.waitForIdle()
        captureScreenRoboImage()
    }

    @Test
    fun albumDetails() {
        val viewModel = AlbumViewModel(
            AlbumReducer(LoadAlbumDetailsUseCase(object : AlbumRepository {
                override suspend fun loadAlbum(albumId: String): ResultWrapper<Album> =
                    ResultWrapper.Success(
                        Album(
                            id = albumId,
                            name = "Thriller",
                            coverUrl = "",
                            isExplicit = false,
                            artistName = "Michael Jackson",
                            trackList = listOf(
                                Track(
                                    "track-1",
                                    "Thriller",
                                    "Michael Jackson",
                                    "Thriller",
                                    "",
                                    false
                                )
                            ),
                        )
                    )
            }))
        )
        composeRule.setContent {
            HiTunesTheme(darkTheme = false) {
                AlbumDetailsScreen(
                    albumId = "album-2",
                    viewModel = viewModel,
                )
            }
        }
        composeRule.waitUntil { viewModel.state.value.album.albumName == "Thriller" }
        composeRule.waitForIdle()
        captureScreenRoboImage()
    }
}
