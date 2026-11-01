package com.skilltest.internal.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseIntent

sealed class AlbumComponentIntent(
    name: String
): BaseIntent(name) {
    data class OpenAlbumDetails(
        val albumId: String,
        val trackId: String,
        val trackName: String,
        val artistId: String,
        val artistName: String,
    ) : AlbumComponentIntent("OpenAlbumDetails")
}
