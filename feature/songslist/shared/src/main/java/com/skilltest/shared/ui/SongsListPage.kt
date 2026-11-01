package com.skilltest.shared.ui

import androidx.compose.runtime.Composable
import com.skilltest.ui.SongsListScreen

@Composable
fun SongListPage(
    onOpenTrackOptions: (trackId: String, albumId: String, trackName: String, artistId: String, artistName: String) -> Unit,
) {
    SongsListScreen(onOpenTrackOptions = onOpenTrackOptions)
}
