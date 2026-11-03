package com.skilltest.shared.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    // Null hides the back arrow, e.g. when the page is a side pane next to the song list.
    onBack: (() -> Unit)?,
    onNavigateToAlbum: (String) -> Unit,
    // The side pane drops the top bar; its menu sits next to the cover instead.
    showTopBar: Boolean = true,
    // Car screens: a short page whose Album button opens the album directly.
    carLayout: Boolean = false,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    var showAlbumMenu by remember { mutableStateOf(false) }
    val playerState by viewModel.state.collectAsState()
    val currentTrack = playerState.track ?: track
    SongDetailsScreen(
        track = track,
        viewModel = viewModel,
        onBack = onBack,
        onMore = { showAlbumMenu = true },
        showTopBar = showTopBar,
        carLayout = carLayout,
        onAlbum = { onNavigateToAlbum(currentTrack.albumId) },
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
    if (showAlbumMenu) {
        AlbumSheetPage(
            trackId = currentTrack.id,
            albumId = currentTrack.albumId,
            trackName = currentTrack.name,
            artistId = currentTrack.artistId,
            artistName = currentTrack.artistName,
            onDismiss = { showAlbumMenu = false },
            onNavigateToAlbumDetails = { albumId ->
                showAlbumMenu = false
                onNavigateToAlbum(albumId)
            },
        )
    }
}
