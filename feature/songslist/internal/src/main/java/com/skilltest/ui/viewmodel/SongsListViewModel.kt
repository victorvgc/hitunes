package com.skilltest.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseViewModel
import timber.log.Timber

class SongsListViewModel(
    songsListReducer: SongsListReducer
) : BaseViewModel<SongsListState, SongsListIntent, SongsListEffect>(
    initialState = SongsListState(),
    reducer = songsListReducer
)
