package com.skilltest.di

import com.skilltest.ui.viewmodel.SongsListReducer
import com.skilltest.ui.viewmodel.SongsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val internalSongsListModule = module {

    factory {
        SongsListReducer()
    }

    viewModel {
        SongsListViewModel(
            get()
        )
    }
}
