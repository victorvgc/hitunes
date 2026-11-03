package com.skilltest.songplayer.internal.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.skilltest.design.component.DetailsTopBar
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
    onBack: () -> Unit,
    onMore: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(track.id) {
        viewModel.onIntent(com.skilltest.songplayer.internal.viewmodel.PlayerIntent.DisableRepeat)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        DetailsTopBar(
            title = (state.track ?: track).name,
            onBack = onBack,
            onMore = onMore,
        )
        SongDetailsContent(
            track = state.track ?: track,
            state = state,
            onIntent = viewModel::onIntent,
            modifier = Modifier.weight(1f),
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}
