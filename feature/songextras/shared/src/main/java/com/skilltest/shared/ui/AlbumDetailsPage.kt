package com.skilltest.shared.ui

import androidx.compose.runtime.Composable
import com.skilltest.internal.ui.AlbumDetailsScreen

@Composable
fun AlbumDetailsPage(
    albumId: String,
) {
    AlbumDetailsScreen(
        albumId = albumId,
    )
}
