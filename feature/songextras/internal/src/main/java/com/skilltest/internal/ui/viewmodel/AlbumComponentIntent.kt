package com.skilltest.internal.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseIntent
import com.skilltest.internal.ui.model.UiAlbum

sealed class AlbumComponentIntent(
    name: String
) : BaseIntent(name) {
    data class OpenAlbumDetails(
        val albumId: String,
        val trackId: String,
        val trackName: String,
        val artistId: String,
        val artistName: String,
    ) : AlbumComponentIntent("OpenAlbumDetails")

    data class LoadAlbumDetails(
        val albumId: String
    ) : AlbumComponentIntent("LoadAlbumDetails")

    data class LoadingDetails(val albumId: String) : AlbumComponentIntent("LoadingDetails")

    data class OnClickTrack(val track: UiAlbum.UiTrack) : AlbumComponentIntent("OnClickTrack")
}
