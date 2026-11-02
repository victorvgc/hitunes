package com.skilltest.navigation

import com.skilltest.domain.model.Track
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SongNavigationTrack(
    val id: String,
    val name: String,
    val artistName: String,
    val albumName: String,
    val artworkUrl: String,
    val isExplicit: Boolean,
    val artistId: String,
    val albumId: String,
    val streamUrl: String,
) {
    fun encode(): String = Json.encodeToString(this)

    fun toTrack(): Track = Track(
        id = id,
        name = name,
        artistName = artistName,
        albumName = albumName,
        artworkUrl = artworkUrl,
        isExplicit = isExplicit,
        artistId = artistId,
        albumId = albumId,
        streamUrl = streamUrl,
    )

    companion object {
        fun decode(value: String): SongNavigationTrack = Json.decodeFromString(value)

        fun from(track: Track): SongNavigationTrack = SongNavigationTrack(
            id = track.id,
            name = track.name,
            artistName = track.artistName,
            albumName = track.albumName,
            artworkUrl = track.artworkUrl,
            isExplicit = track.isExplicit,
            artistId = track.artistId,
            albumId = track.albumId,
            streamUrl = track.streamUrl,
        )
    }
}
