package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseIntent

sealed class SongsListIntent(
    override val name: String,
) : BaseIntent(name) {

    data object LoadRecentlyPlayed : SongsListIntent(name = "LoadRecentlyPlayed")
    data class LoadSearchResults(val term: String) : SongsListIntent(name = "LoadSearchResults")
    data object SearchClick : SongsListIntent("SearchClick")
    data object ClearSearch : SongsListIntent("ClearSearch")
    data class OpenMoreOptionsForTrack(val trackId: String) :
        SongsListIntent("OpenMoreOptionsForTrack")

    data class OpenDetailsForTrack(val trackId: String) : SongsListIntent("OpenDetailsForTrack")
}
