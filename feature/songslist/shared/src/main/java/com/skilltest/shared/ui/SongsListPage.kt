package com.skilltest.shared.ui

import androidx.compose.runtime.Composable
import com.skilltest.navigation.Route
import com.skilltest.ui.SongsListScreen

@Composable
fun SongListPage(
    navigateToPage: (Route) -> Unit,
) {
    SongsListScreen(navigateToPage = navigateToPage)
}
