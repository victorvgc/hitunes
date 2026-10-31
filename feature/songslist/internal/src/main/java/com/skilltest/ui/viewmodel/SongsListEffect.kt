package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseUiEffect

sealed class SongsListEffect(
    override val name: String,
) : BaseUiEffect(name) {
    data class NavigateToTrackDetails(val id: String) :
        SongsListEffect(name = "NavigateToTrackDetails")

    data class NavigateToTrackOptions(val id: String) :
        SongsListEffect(name = "NavigateToTrackOptions")
}
