package com.skilltest.di

import com.skilltest.data.datasource.SongsListDataSource
import com.skilltest.data.local.LocalSongsListDataSource
import com.skilltest.data.remote.SongsListService
import com.skilltest.data.remote.datasource.RemoteSongsListDataSourceImpl
import com.skilltest.data.repository.SongsListRepositoryImpl
import com.skilltest.domain.repository.SongsListRepository
import com.skilltest.domain.usecase.SearchSongsByTermUseCase
import com.skilltest.domain.usecase.ShouldShowSearchLoadingUseCase
import com.skilltest.ui.viewmodel.SongsListReducer
import com.skilltest.ui.viewmodel.SongsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

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

    single<SongsListService> {
        get<Retrofit>().create(SongsListService::class.java)
    }

    single<SongsListDataSource.Remote> {
        RemoteSongsListDataSourceImpl(
            get()
        )
    }

    single<SongsListDataSource.Local> {
        LocalSongsListDataSource()
    }

    single<SongsListRepository> {
        SongsListRepositoryImpl(
            get(),
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
