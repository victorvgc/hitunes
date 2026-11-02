package com.skilltest.data.remote.di

import com.skilltest.data.datasource.AlbumDataSource
import com.skilltest.data.datasource.SongDataSource
import com.skilltest.data.remote.datasource.RemoteAlbumDataSource
import com.skilltest.data.remote.service.SongService
import com.skilltest.data.remote.datasource.RemoteSongDataSourceImpl
import com.skilltest.data.remote.service.AlbumService
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteDataSourceModule = module {
    single<SongService> { get<Retrofit>().create(SongService::class.java) }
    single<SongDataSource.Remote> { RemoteSongDataSourceImpl(get()) }
    single<AlbumService> { get<Retrofit>().create(AlbumService::class.java) }
    single<AlbumDataSource.Remote> { RemoteAlbumDataSource(get()) }
}
