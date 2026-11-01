package com.skilltest.internal.ui.di

import com.skilltest.internal.ui.viewmodel.AlbumReducer
import com.skilltest.internal.ui.viewmodel.AlbumViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val albumModule = module {
    factory { AlbumReducer() }
    viewModel { AlbumViewModel(get()) }
}
