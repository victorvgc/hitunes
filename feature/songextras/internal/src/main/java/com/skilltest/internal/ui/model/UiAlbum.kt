package com.skilltest.internal.ui.model

import com.skilltest.domain.model.Album
import com.skilltest.domain.model.Track
import com.skilltest.internal.ui.model.UiAlbum.UiTrack.Companion.toUI

data class UiAlbum(
    val albumName: String = "",
    val albumCoverUrl: String = "",
    val artistName: String = "",
    val trackList: List<UiTrack> = emptyList(),
) {
    data class UiTrack(
        val trackId: String,
        val trackName: String,
        val artistName: String,
        val artwork: String,
        val albumId: String = "",
        val albumName: String = "",
        val artistId: String = "",
        val streamUrl: String = "",
        val isExplicit: Boolean = false,
    ) {
        companion object {
            fun Track.toUI() = UiTrack(
                trackId = id,
                trackName = name,
                artistName = artistName,
                artwork = artworkUrl,
                albumId = albumId,
                albumName = albumName,
                artistId = artistId,
                streamUrl = streamUrl,
                isExplicit = isExplicit,
            )
        }

        fun toDomain() = Track(
            id = trackId,
            name = trackName,
            artistName = artistName,
            albumName = albumName,
            artworkUrl = artwork,
            isExplicit = isExplicit,
            artistId = artistId,
            albumId = albumId,
            streamUrl = streamUrl,
        )
    }

    companion object {
        fun Album.toUi() = UiAlbum(
            albumName = name,
            albumCoverUrl = coverUrl,
            artistName = artistName,
            trackList = trackList.map { it.toUI() }
        )
    }
}
