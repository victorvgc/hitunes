package com.skilltest.hitunes.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.domain.model.Track
import com.skilltest.hitunes.ui.layout.AppLayout
import com.skilltest.hitunes.ui.layout.isAutomotive
import com.skilltest.hitunes.ui.layout.isPhone
import com.skilltest.hitunes.ui.layout.rememberAppLayout
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

        // Landscape is a tablet (and car) layout only; phones stay in portrait.
        if (isPhone(resources.configuration.smallestScreenWidthDp) && !isAutomotive()) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        setContent {
            val navController = rememberNavController()
            val controller by rememberManagedMediaController()
            val playerViewModel: PlayerViewModel = koinViewModel<PlayerViewModel>()
            val playerState by playerViewModel.state.collectAsState()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val onSongPage = backStackEntry?.destination?.route
                ?.startsWith(Route.SongDetails::class.qualifiedName.orEmpty()) == true
            val twoPane = rememberAppLayout() == AppLayout.TwoPane

            // The song the user picked, kept like the song page route keeps it so the side pane
            // behaves the same: skipping to another queued song doesn't count as opening it.
            var openedTrack by rememberSaveable { mutableStateOf<String?>(null) }

            val playTrack: (Track, List<Track>) -> Unit = { track, queue ->
                openedTrack = Route.SongDetails.from(track).track
                playerViewModel.onIntent(PlayerIntent.PlayTrack(track, queue))
            }
            // In two panes the playing song is already shown next to the list.
            val navigate: (Route) -> Unit = { route ->
                if (!(twoPane && route is Route.SongDetails)) navController.navigate(route)
            }

            LaunchedEffect(controller) {
                playerViewModel.bind(controller)
            }

            LaunchedEffect(twoPane, onSongPage) {
                if (twoPane && onSongPage) navController.popBackStack()
            }

            HiTunesTheme {
                SharedTransitionLayout {
                    Scaffold(
                        bottomBar = {
                            AnimatedVisibility(
                                visible = !twoPane && !onSongPage && playerState.track != null,
                                enter = fadeIn(),
                                exit = fadeOut(),
                            ) {
                                MiniPlayerPage(
                                    viewModel = playerViewModel,
                                    onOpenSong = { track ->
                                        navigate(Route.SongDetails.from(track))
                                    },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                )
                            }
                        }
                    ) { innerPadding ->
                        Row(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            NavHost(
                                modifier = Modifier.weight(1f),
                                navController = navController,
                                startDestination = Route.SongsList
                            ) {
                                composable<Route.SongsList> {
                                    SongListPage(
                                        navigateToPage = navigate,
                                        onPlayTrack = playTrack,
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedVisibilityScope = this@composable,
                                    )
                                }
                                composable<Route.Album> { entry ->
                                    val album = entry.toRoute<Route.Album>()
                                    AlbumDetailsPage(
                                        albumId = album.albumId,
                                        onBack = { navController.popBackStack() },
                                        onTrackSelected = { track, queue ->
                                            playTrack(track, queue)
                                            navigate(Route.SongDetails.from(track))
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
                                            navigate(Route.Album(albumId))
                                        },
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedVisibilityScope = this@composable,
                                    )
                                }
                            }

                            AnimatedVisibility(
                                visible = twoPane && playerState.track != null,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.5f),
                                enter = expandHorizontally() + fadeIn(),
                                exit = shrinkHorizontally() + fadeOut(),
                            ) {
                                val track = openedTrack?.let { Route.SongDetails(it).toTrack() }
                                    ?: playerState.track
                                if (track != null) {
                                    Row {
                                        VerticalDivider()
                                        // No shared element here: the list row showing the
                                        // same artwork stays on screen next to this pane.
                                        SongDetailsPage(
                                            track = track,
                                            viewModel = playerViewModel,
                                            onBack = null,
                                            showTopBar = false,
                                            onNavigateToAlbum = { albumId ->
                                                navigate(Route.Album(albumId))
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
