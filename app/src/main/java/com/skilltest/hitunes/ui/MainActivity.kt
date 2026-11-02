package com.skilltest.hitunes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.navigation.Route
import com.skilltest.shared.rememberManagedMediaController
import com.skilltest.shared.ui.AlbumDetailsPage
import com.skilltest.shared.ui.MiniPlayerPage
import com.skilltest.shared.ui.SongDetailsPage
import com.skilltest.shared.ui.SongListPage
import com.skilltest.songplayer.internal.viewmodel.PlayerIntent
import com.skilltest.songplayer.internal.viewmodel.PlayerViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val controller by rememberManagedMediaController()
            val playerViewModel: PlayerViewModel = koinViewModel()
            val playerState by playerViewModel.state.collectAsState()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val onSongPage = backStackEntry?.destination?.route
                ?.startsWith(Route.SongDetails::class.qualifiedName.orEmpty()) == true

            LaunchedEffect(controller) {
                playerViewModel.bind(controller)
            }

            HiTunesTheme {
                Scaffold(
                    bottomBar = {
                        if (!onSongPage && playerState.track != null) {
                            MiniPlayerPage(playerViewModel) { track ->
                                navController.navigate(Route.SongDetails.from(track))
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Route.SongsList
                    ) {
                        composable<Route.SongsList> {
                            SongListPage(
                                navigateToPage = navController::navigate,
                                onPlayTrack = { track, queue ->
                                    playerViewModel.onIntent(PlayerIntent.PlayTrack(track, queue))
                                },
                            )
                        }
                        composable<Route.Album> { entry ->
                            val album = entry.toRoute<Route.Album>()
                            AlbumDetailsPage(
                                albumId = album.albumId,
                            )
                        }
                        composable<Route.SongDetails> { entry ->
                            val song = entry.toRoute<Route.SongDetails>()
                            SongDetailsPage(song.toTrack(), playerViewModel)
                        }
                    }
                }
            }
        }
    }

}
