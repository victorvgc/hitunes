package com.skilltest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.skilltest.design.screen.LoadingScreen
import com.skilltest.navigation.Route
import com.skilltest.shared.ui.AlbumSheetPage
import com.skilltest.ui.model.UiTrack
import com.skilltest.ui.model.UiTrack.Companion.toDomain
import com.skilltest.ui.viewmodel.SongsListViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SongsListScreen(
    modifier: Modifier = Modifier,
    navigateToPage: (Route) -> Unit,
    onPlayTrack: (com.skilltest.domain.model.Track, List<com.skilltest.domain.model.Track>) -> Unit,
    viewModel: SongsListViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val currentState by rememberUpdatedState(state)

    var showingAlbumSheet by remember {
        mutableStateOf<UiTrack?>(null)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is com.skilltest.ui.viewmodel.SongsListEffect.NavigateToTrackOptions -> {
                    showingAlbumSheet = effect.track
                }
                is com.skilltest.ui.viewmodel.SongsListEffect.NavigateToTrackDetails -> {
                    val track = effect.track
                    if (track.streamUrl.isNotBlank()) {
                        val selectedTrack = track.toDomain()
                        val visible =
                            if (!currentState.isSearching || currentState.searchTerm.isEmpty()) {
                                currentState.recentlyPlayed
                            } else {
                                currentState.searchResult
                            }
                        val queue =
                            visible.filter { it.streamUrl.isNotBlank() }.map { it.toDomain() }
                        onPlayTrack(selectedTrack, queue)
                        navigateToPage(Route.SongDetails.from(selectedTrack))
                    }
                }
            }
        }
    }

    when {
        state.isError -> Unit
        state.isLoading -> {
            LoadingScreen()
        }
        else -> SongsListContent(
            state = state,
            onIntent = viewModel::onIntent
        )
    }

    showingAlbumSheet?.let {
        AlbumSheetPage(
            trackId = it.id,
            albumId = it.albumId,
            trackName = it.name,
            artistId = it.artistId,
            artistName = it.artistName,
            onDismiss = { showingAlbumSheet = null },
            onNavigateToAlbumDetails = { albumId ->
                showingAlbumSheet = null
                navigateToPage(
                    Route.Album(albumId = albumId)
                )
            },
        )
    }
}
