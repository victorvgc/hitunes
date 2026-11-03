package com.skilltest.internal.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseUiEffect
import com.skilltest.domain.model.Track

sealed class AlbumComponentEffect(
    name: String
): BaseUiEffect(name) {
    data class NavigateToAlbumDetails(
        val albumId: String,
        val trackId: String,
        val trackName: String,
        val artistId: String,
        val artistName: String,
    ) : AlbumComponentEffect("NavigateToAlbumDetails")

    data class NavigateToSongDetails(
        val track: Track,
    ) : AlbumComponentEffect("NavigateToSongDetails")
}
