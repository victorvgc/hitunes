package com.skilltest.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object SongsList : Route

    @Serializable
    data object SongDetails : Route

    @Serializable
    data object Album : Route

    @Serializable
    data object SongOptions : Route
}
