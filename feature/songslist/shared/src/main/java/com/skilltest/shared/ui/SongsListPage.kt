package com.skilltest.shared.ui

import androidx.compose.runtime.Composable
import com.skilltest.navigation.Route
import com.skilltest.ui.SongsListScreen

@Composable
fun SongListPage(
    navigateToPage: (Route) -> Unit,
    onPlayTrack: (com.skilltest.domain.model.Track, List<com.skilltest.domain.model.Track>) -> Unit,
) {
    SongsListScreen(navigateToPage = navigateToPage, onPlayTrack = onPlayTrack)
}
