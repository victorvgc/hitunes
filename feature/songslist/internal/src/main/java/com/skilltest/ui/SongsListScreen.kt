package com.skilltest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.skilltest.design.screen.LoadingScreen
import com.skilltest.ui.viewmodel.SongsListIntent
import com.skilltest.ui.viewmodel.SongsListViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun SongsListScreen(
    modifier: Modifier = Modifier,
    viewModel: SongsListViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.onIntent(SongsListIntent.LoadRecentlyPlayed)
    }

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            Timber.d("Effect received: ${effect.name}")
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
}
