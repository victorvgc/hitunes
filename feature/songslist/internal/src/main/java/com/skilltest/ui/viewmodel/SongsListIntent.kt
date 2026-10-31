package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseIntent

sealed class SongsListIntent(
    override val name: String,
) : BaseIntent(name) {

    data object LoadRecentlyPlayed : SongsListIntent(name = "LoadRecentlyPlayed")
    data object LoadingRecentlyPlayed : SongsListIntent(name = "LoadingRecentlyPlayed")
    data class PerformSearch(val term: String) : SongsListIntent(name = "LoadSearchResults")
    data class StartPerformSearch(val term: String) : SongsListIntent(name = "StartPerformSearch")
    data class TypingSearchTerm(val term: String) : SongsListIntent(name = "TypingSearchTerm")
    data object SearchClick : SongsListIntent("SearchClick")
    data object ClearSearch : SongsListIntent("ClearSearch")
    data class OpenTrackMoreOptions(val trackId: String) :
        SongsListIntent("OpenMoreOptionsForTrack")

    data class OpenTrackDetails(val trackId: String) : SongsListIntent("OpenDetailsForTrack")
}
