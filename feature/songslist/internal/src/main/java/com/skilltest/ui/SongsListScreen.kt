package com.skilltest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.skilltest.design.screen.LoadingScreen
import com.skilltest.navigation.Route
import com.skilltest.shared.ui.AlbumSheetPage
import com.skilltest.ui.model.UiTrack
import com.skilltest.ui.viewmodel.SongsListViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun SongsListScreen(
    modifier: Modifier = Modifier,
    navigateToPage: (Route) -> Unit,
    viewModel: SongsListViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState().value

    var showingAlbumSheet by remember {
        mutableStateOf<UiTrack?>(null)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is com.skilltest.ui.viewmodel.SongsListEffect.NavigateToTrackOptions -> {
                    showingAlbumSheet = effect.track
                }
                else -> Timber.d("Effect received: ${effect.name}")
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
