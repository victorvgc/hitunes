package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class SongsListViewModel : BaseViewModel<SongsListState, SongsListIntent, SongsListEffect>(
    reducer = SongsListReducer()
) {
    override fun Flow<SongsListState>.handleAnalytics(
        state: SongsListState,
        intent: SongsListIntent
    ) {
        Timber.d("handling interaction: ${state.name} + ${intent.name}")
    }

}
