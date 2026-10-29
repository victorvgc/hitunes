package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseReducer

class SongsListReducer() : BaseReducer<SongsListState, SongsListIntent, SongsListEffect>(
    SongsListState()
) {
    override suspend fun handleIntent(
        state: SongsListState,
        intent: SongsListIntent
    ): SongsListState {
        when (intent) {
            SongsListIntent.LoadRecentlyPlayed -> TODO()
            is SongsListIntent.LoadSearchResults -> TODO()
            SongsListIntent.SearchClick -> TODO()
            SongsListIntent.ClearSearch -> TODO()
        }
    }
}
