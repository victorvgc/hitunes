package com.skilltest.shared.ui

import androidx.compose.runtime.Composable
import com.skilltest.internal.ui.AlbumDetails

@Composable
fun AlbumDetailsPage(
    albumId: String,
    trackId: String,
    trackName: String,
    artistId: String,
    artistName: String,
) {
    AlbumDetails(
        albumId = albumId,
        trackId = trackId,
        trackName = trackName,
        artistId = artistId,
        artistName = artistName,
    )
}
