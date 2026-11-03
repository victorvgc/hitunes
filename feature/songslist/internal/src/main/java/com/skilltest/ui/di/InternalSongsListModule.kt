package com.skilltest.ui.di

import com.skilltest.ui.viewmodel.SongsListReducer
import com.skilltest.ui.viewmodel.SongsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val internalSongsListModule = module {

    factory {
        SongsListReducer(
            get(),
            get(),
            get()
        )
    }

    viewModel {
        SongsListViewModel(
            get()
        )
    }
}
