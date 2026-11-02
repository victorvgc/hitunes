package com.skilltest.hitunes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
                Scaffold() { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Route.SongsList
                    ) {
                        composable<Route.SongsList> {
                            SongListPage { route ->
                                navController.navigate(route)
                            }
                        }
                        composable<Route.Album> { entry ->
                            val album = entry.toRoute<Route.Album>()
                            AlbumDetailsPage(
                                albumId = album.albumId,
                            )
                        }
                    }
                }
            }
        }
    }
}
