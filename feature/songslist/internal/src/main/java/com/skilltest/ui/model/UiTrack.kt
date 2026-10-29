package com.skilltest.ui.model

import com.skilltest.domain.model.Track

data class UiTrack(
    val id: String,
    val name: String,
    val artistName: String,
    val artwork: String,
    val isExplicit: Boolean
) {
    companion object {
        fun Track.toUi(): UiTrack = UiTrack(
            id = id,
            name = name,
            artistName = artistName,
            artwork = artworkUrl,
            isExplicit = isExplicit
        )
    }
}
