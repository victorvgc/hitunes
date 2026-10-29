package com.skilltest.ui.viewmodel

import androidx.compose.runtime.Immutable
import com.skilltest.core.presentation.viewmodel.BaseUiState
import com.skilltest.ui.model.UiTrack

@Immutable
data class SongsListState(
    override val name: String = "SongsListState",
    val isLoading: Boolean = true,
    val searchResult: List<UiTrack> = emptyList(),
    val searchTerm: String = "",
    val recentlyPlayed: List<UiTrack> = emptyList(),
    val isError: Boolean = false,
    val isSearching: Boolean = false,
) : BaseUiState(name)
