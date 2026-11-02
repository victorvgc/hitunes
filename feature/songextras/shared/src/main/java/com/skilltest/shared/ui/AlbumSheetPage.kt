package com.skilltest.shared.ui

import androidx.compose.runtime.Composable
import com.skilltest.core.presentation.viewmodel.Reduction.Companion.effect
import com.skilltest.internal.ui.AlbumSheet
import com.skilltest.internal.ui.model.UiTrackAlbumInfo
import com.skilltest.internal.ui.viewmodel.AlbumComponentEffect

@Composable
fun AlbumSheetPage(
    trackId: String,
    albumId: String,
    trackName: String,
    artistId: String,
    artistName: String,
    onDismiss: () -> Unit,
    onNavigateToAlbumDetails: (albumId: String) -> Unit,
) {
    AlbumSheet(
        trackAlbumInfo = UiTrackAlbumInfo(trackId, albumId, trackName, artistId, artistName),
        onDismiss = onDismiss,
        onNavigateToAlbumDetails = { albumId ->
            onNavigateToAlbumDetails(albumId)
        },
    )
}
