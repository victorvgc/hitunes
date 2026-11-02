package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseUiEffect
import com.skilltest.ui.model.UiTrack

sealed class SongsListEffect(
    name: String,
) : BaseUiEffect(name) {
    data class NavigateToTrackDetails(val id: String) :
        SongsListEffect(name = "NavigateToTrackDetails")

    data class NavigateToTrackOptions(val track: UiTrack) :
        SongsListEffect(name = "NavigateToTrackOptions")
}
