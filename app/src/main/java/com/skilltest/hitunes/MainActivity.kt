package com.skilltest.hitunes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.skilltest.design.theme.HiTunesTheme
import com.skilltest.navigation.Route
import com.skilltest.shared.ui.SongListPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            HiTunesTheme {
                NavHost(navController, startDestination = Route.SongsList) {
                    composable<Route.SongsList> {
                        SongListPage()
                    }
                    dialog<Route.SongOptions> {
                        // todo: More options bottom sheet
                    }
                }
            }
        }
    }
}
