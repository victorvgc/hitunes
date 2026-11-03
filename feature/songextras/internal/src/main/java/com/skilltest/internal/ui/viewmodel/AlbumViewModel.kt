package com.skilltest.internal.ui.viewmodel

import com.skilltest.core.presentation.viewmodel.BaseViewModel
class AlbumViewModel(
    reducer: AlbumReducer,
) : BaseViewModel<AlbumComponentState, AlbumComponentIntent, AlbumComponentEffect>(
    initialState = AlbumComponentState(),
    reducer = reducer,
)
