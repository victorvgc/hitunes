package com.skilltest.hitunes.navigation.page

sealed interface NavPage {
    data object Splash : NavPage
    data object SongsList : NavPage
}
