package com.skilltest.shared.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import com.skilltest.domain.model.Track
import com.skilltest.songplayer.internal.ui.MiniPlayerScreen
import com.skilltest.songplayer.internal.ui.SongDetailsScreen
import com.skilltest.songplayer.internal.viewmodel.PlayerViewModel

@Composable
fun MiniPlayerPage(
    viewModel: PlayerViewModel,
    onOpenSong: (Track) -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    MiniPlayerScreen(viewModel, onOpenSong, sharedTransitionScope, animatedVisibilityScope)
}

@Composable
fun SongDetailsPage(
    track: Track,
    viewModel: PlayerViewModel,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    SongDetailsScreen(track, viewModel, sharedTransitionScope, animatedVisibilityScope)
}
