package com.skilltest.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object SongsList : Route

    @Serializable
    data object SongDetails : Route

    @Serializable
    data class Album(
        val albumId: String,
    ) : Route

    @Serializable
    data object SongOptions : Route
}
