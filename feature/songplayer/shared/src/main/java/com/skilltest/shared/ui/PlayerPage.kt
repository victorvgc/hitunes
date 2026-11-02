package com.skilltest.shared.ui

import androidx.compose.runtime.Composable
import com.skilltest.domain.model.Track
import com.skilltest.songplayer.internal.ui.MiniPlayerScreen
import com.skilltest.songplayer.internal.ui.SongDetailsScreen
import com.skilltest.songplayer.internal.viewmodel.PlayerViewModel

@Composable
fun MiniPlayerPage(viewModel: PlayerViewModel, onOpenSong: (Track) -> Unit) {
    MiniPlayerScreen(viewModel, onOpenSong)
}

@Composable
fun SongDetailsPage(track: Track, viewModel: PlayerViewModel) {
    SongDetailsScreen(track, viewModel)
}
