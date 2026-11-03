package com.skilltest.shared.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import com.skilltest.domain.model.Track
import com.skilltest.navigation.Route
import com.skilltest.ui.SongsListScreen

@Composable
fun SongListPage(
    navigateToPage: (Route) -> Unit,
    onPlayTrack: (Track, List<Track>) -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    SongsListScreen(
        navigateToPage = navigateToPage,
        onPlayTrack = onPlayTrack,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}
