package com.skilltest.songplayer.internal.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.skilltest.domain.model.Track
import com.skilltest.songplayer.internal.viewmodel.PlayerViewModel

@Composable
fun MiniPlayerScreen(viewModel: PlayerViewModel, onOpenSong: (Track) -> Unit) {
    val state by viewModel.state.collectAsState()
    MiniPlayerContent(state, viewModel::onIntent) {
        state.track?.let(onOpenSong)
    }
}

@Composable
fun SongDetailsScreen(track: Track, viewModel: PlayerViewModel) {
    val state by viewModel.state.collectAsState()
    SongDetailsContent(state.track ?: track, state, viewModel::onIntent)
}
