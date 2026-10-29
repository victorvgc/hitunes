package com.skilltest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.skilltest.ui.viewmodel.SongsListViewModel

@Composable
fun SongsListScreen(
    modifier: Modifier = Modifier,
    viewModel: SongsListViewModel = SongsListViewModel() // todo: dependency injection
) {
    val state = viewModel.state.collectAsState().value

    when {
        state.isError -> Unit
        else -> SongsListContent(
            state = state,
            onIntent = viewModel::onIntent
        )
    }
}
