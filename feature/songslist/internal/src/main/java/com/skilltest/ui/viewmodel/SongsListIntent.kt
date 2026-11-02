package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseIntent
import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.ui.model.UiTrack

sealed class SongsListIntent(
    name: String,
) : BaseIntent(name) {

    data object LoadRecentlyPlayed : SongsListIntent(name = "LoadRecentlyPlayed")
    data class RecentlyPlayedChanged(val result: ResultWrapper<RecentlyPlayed>) :
        SongsListIntent(name = "RecentlyPlayedChanged")
    data class PerformSearch(val term: String) : SongsListIntent(name = "LoadSearchResults")
    data class StartPerformSearch(val term: String) : SongsListIntent(name = "StartPerformSearch")
    data class TypingSearchTerm(val term: String) : SongsListIntent(name = "TypingSearchTerm")
    data object SearchClick : SongsListIntent("SearchClick")
    data object ClearSearch : SongsListIntent("ClearSearch")
    data class OpenTrackMoreOptions(val track: UiTrack) :
        SongsListIntent("OpenMoreOptionsForTrack")

    data class OpenTrackDetails(val track: UiTrack) : SongsListIntent("OpenDetailsForTrack")
}
