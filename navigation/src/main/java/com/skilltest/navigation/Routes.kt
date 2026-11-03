package com.skilltest.navigation

import com.skilltest.domain.model.Track
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object SongsList : Route

    @Serializable
    data class SongDetails(val track: String) : Route {
        fun toTrack(): Track = SongNavigationTrack.decode(track).toTrack()

        companion object {
            fun from(track: Track): SongDetails =
                SongDetails(SongNavigationTrack.from(track).encode())
        }
    }

    @Serializable
    data class Album(
        val albumId: String,
    ) : Route

    @Serializable
    data object SongOptions : Route
}
