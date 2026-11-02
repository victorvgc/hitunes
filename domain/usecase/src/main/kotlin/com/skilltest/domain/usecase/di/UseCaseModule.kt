package com.skilltest.domain.usecase.di

import com.skilltest.domain.usecase.AddToRecentlyPlayedUseCase
import com.skilltest.domain.usecase.GetRecentlyPlayedUseCase
import com.skilltest.domain.usecase.LoadAlbumDetailsUseCase
import com.skilltest.domain.usecase.SearchSongsByTermUseCase
import com.skilltest.domain.usecase.ShouldShowSearchLoadingUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { SearchSongsByTermUseCase(get()) }
    factory { ShouldShowSearchLoadingUseCase() }
    factory { LoadAlbumDetailsUseCase(get()) }
    factory { GetRecentlyPlayedUseCase(get()) }
    factory { AddToRecentlyPlayedUseCase(get()) }
}
