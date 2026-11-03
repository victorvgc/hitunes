package com.skilltest.hitunes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
            val playerViewModel: PlayerViewModel = koinViewModel<PlayerViewModel>()
            val playerState by playerViewModel.state.collectAsState()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val onSongPage = backStackEntry?.destination?.route
                ?.startsWith(Route.SongDetails::class.qualifiedName.orEmpty()) == true

            LaunchedEffect(controller) {
                playerViewModel.bind(controller)
            }

            HiTunesTheme {
                SharedTransitionLayout {
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(
                                visible = !onSongPage && playerState.track != null,
                                enter = fadeIn(),
                                exit = fadeOut(),
                            ) {
                                MiniPlayerPage(
                                    viewModel = playerViewModel,
                                    onOpenSong = { track ->
                                        navController.navigate(Route.SongDetails.from(track))
                                    },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                )
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
                                        playerViewModel.onIntent(
                                            PlayerIntent.PlayTrack(
                                                track,
                                                queue
                                            )
                                        )
                                    },
                                )
                            }
                            composable<Route.Album> { entry ->
                                val album = entry.toRoute<Route.Album>()
                                AlbumDetailsPage(
                                    albumId = album.albumId,
                                    onBack = { navController.popBackStack() },
                                    onTrackSelected = { track, queue ->
                                        playerViewModel.onIntent(
                                            PlayerIntent.PlayTrack(
                                                track,
                                                queue
                                            )
                                        )
                                        navController.navigate(Route.SongDetails.from(track))
                                    },
                                )
                            }
                            composable<Route.SongDetails> { entry ->
                                val song = entry.toRoute<Route.SongDetails>()
                                SongDetailsPage(
                                    track = song.toTrack(),
                                    viewModel = playerViewModel,
                                    onBack = { navController.popBackStack() },
                                    onNavigateToAlbum = { albumId ->
                                        navController.navigate(Route.Album(albumId))
                                    },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@composable,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}
