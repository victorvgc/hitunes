package com.skilltest.di

import com.skilltest.domain.usecase.SearchSongsByTermUseCase
import com.skilltest.domain.usecase.ShouldShowSearchLoadingUseCase
import com.skilltest.ui.viewmodel.SongsListReducer
import com.skilltest.ui.viewmodel.SongsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val internalSongsListModule = module {

    factory {
        SongsListReducer(
            get(),
            get()
        )
    }

    viewModel {
        SongsListViewModel(
            get()
        )
    }

    single {
        SearchSongsByTermUseCase(
            get()
        )
    }

    single {
        ShouldShowSearchLoadingUseCase()
    }
}
