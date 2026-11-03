package com.skilltest.ui.model

import com.skilltest.domain.model.Track

data class UiTrack(
    val id: String,
    val name: String,
    val artistName: String,
    val artwork: String,
    val isExplicit: Boolean,
    val artistId: String = "",
    val albumId: String = "",
    val streamUrl: String = "",
    val albumName: String = "",
) {
    companion object {
        fun Track.toUi(): UiTrack = UiTrack(
            id = id,
            name = name,
            artistName = artistName,
            artwork = artworkUrl,
            isExplicit = isExplicit,
            artistId = artistId,
            albumId = albumId,
            streamUrl = streamUrl,
            albumName = albumName,
        )

        fun UiTrack.toDomain(): Track = Track(
            id = id,
            name = name,
            artistName = artistName,
            albumName = albumName,
            artworkUrl = artwork,
            isExplicit = isExplicit,
            artistId = artistId,
            albumId = albumId,
            streamUrl = streamUrl,
        )
    }
}
