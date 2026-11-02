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
    ) {
        companion object {
            fun Track.toUI() = UiTrack(
                trackId = id,
                trackName = name,
                artistName = artistName,
                artwork = artworkUrl
            )
        }
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
