package com.skilltest.songplayer.internal.di

import com.skilltest.songplayer.internal.viewmodel.PlayerReducer
import com.skilltest.songplayer.internal.viewmodel.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val internalPlayerModule = module {
    factory { PlayerReducer(get()) }
    viewModel { PlayerViewModel(get()) }
}
