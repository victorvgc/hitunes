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
        val trackId: String,
        val trackName: String,
        val artistId: String,
        val artistName: String,
    ) : Route

    @Serializable
    data object SongOptions : Route
}
