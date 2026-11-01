package com.skilltest.data.remote.di

import com.skilltest.data.datasource.SongsListDataSource
import com.skilltest.data.remote.SongsListService
import com.skilltest.data.remote.datasource.RemoteSongsListDataSourceImpl
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteDataSourceModule = module {
    single<SongsListService> { get<Retrofit>().create(SongsListService::class.java) }
    single<SongsListDataSource.Remote> { RemoteSongsListDataSourceImpl(get()) }
}
