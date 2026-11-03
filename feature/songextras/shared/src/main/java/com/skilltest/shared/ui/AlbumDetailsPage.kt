package com.skilltest.shared.ui

import androidx.compose.runtime.Composable
import com.skilltest.domain.model.Track
import com.skilltest.internal.ui.AlbumDetailsScreen

@Composable
fun AlbumDetailsPage(
    albumId: String,
    onBack: () -> Unit,
    onTrackSelected: (Track, List<Track>) -> Unit,
) {
    AlbumDetailsScreen(
        albumId = albumId,
        onBack = onBack,
        onTrackSelected = onTrackSelected,
    )
}
