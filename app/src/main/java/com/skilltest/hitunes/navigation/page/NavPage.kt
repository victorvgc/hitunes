package com.skilltest.hitunes.navigation.page

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavPage {

    @Serializable
    data object SongsList : NavPage

    @Serializable
    data object SongDetails : NavPage

    @Serializable
    data object Album : NavPage
}
