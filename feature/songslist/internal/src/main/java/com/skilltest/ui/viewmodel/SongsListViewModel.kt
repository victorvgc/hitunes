package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class SongsListViewModel(
    songsListReducer: SongsListReducer
) : BaseViewModel<SongsListState, SongsListIntent, SongsListEffect>(
    initialState = SongsListState(),
    reducer = songsListReducer
) {
    override suspend fun handleAnalytics(
        state: SongsListState,
        intent: SongsListIntent,
        effect: SongsListEffect?
    ) {
        var msg = "State: ${state.name} => Intent: ${intent.name}"

        effect?.let {
            msg += " created Effect: ${effect.name}"
        }

        Timber.d(msg)
    }
}
