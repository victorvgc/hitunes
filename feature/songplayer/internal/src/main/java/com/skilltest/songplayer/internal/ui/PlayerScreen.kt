package com.skilltest.songplayer.internal.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.skilltest.domain.model.Track
import com.skilltest.songplayer.internal.viewmodel.PlayerViewModel

@Composable
fun MiniPlayerScreen(
    viewModel: PlayerViewModel,
    onOpenSong: (Track) -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val state by viewModel.state.collectAsState()
    MiniPlayerContent(
        state = state,
        onIntent = viewModel::onIntent,
        onOpenSong = { state.track?.let(onOpenSong) },
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}

@Composable
fun SongDetailsScreen(
    track: Track,
    viewModel: PlayerViewModel,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val state by viewModel.state.collectAsState()
    SongDetailsContent(
        track = state.track ?: track,
        state = state,
        onIntent = viewModel::onIntent,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}
