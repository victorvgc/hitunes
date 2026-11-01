package com.skilltest.hitunes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.navigation.Route
import com.skilltest.shared.ui.SongListPage
import com.skilltest.shared.ui.AlbumDetailsPage
import com.skilltest.shared.ui.AlbumSheetPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            HiTunesTheme {
                var selectedAlbum by remember { mutableStateOf<Route.Album?>(null) }

                NavHost(navController, startDestination = Route.SongsList) {
                    composable<Route.SongsList> {
                        SongListPage { trackId, albumId, trackName, artistId, artistName ->
                            selectedAlbum = Route.Album(
                                albumId = albumId,
                                trackId = trackId,
                                trackName = trackName,
                                artistId = artistId,
                                artistName = artistName,
                            )
                        }
                    }
                    composable<Route.Album> { entry ->
                        val album = entry.toRoute<Route.Album>()
                        AlbumDetailsPage(
                            albumId = album.albumId,
                            trackId = album.trackId,
                            trackName = album.trackName,
                            artistId = album.artistId,
                            artistName = album.artistName,
                        )
                    }
                }

                selectedAlbum?.let { album ->
                    AlbumSheetPage(
                        trackId = album.trackId,
                        albumId = album.albumId,
                        trackName = album.trackName,
                        artistId = album.artistId,
                        artistName = album.artistName,
                        onDismiss = { selectedAlbum = null },
                        onNavigateToAlbumDetails = { albumId, trackId, trackName, artistId, artistName ->
                            selectedAlbum = null
                            navController.navigate(
                                Route.Album(
                                    albumId = albumId,
                                    trackId = trackId,
                                    trackName = trackName,
                                    artistId = artistId,
                                    artistName = artistName,
                                )
                            )
                        },
                    )
                }
            }
        }
    }
}
