package com.skilltest.data.di

import com.skilltest.data.repository.AlbumRepositoryImpl
import com.skilltest.data.repository.SongRepositoryImpl
import com.skilltest.domain.repository.AlbumRepository
import com.skilltest.domain.repository.SongRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<SongRepository> { SongRepositoryImpl(get(), get()) }
    single<AlbumRepository> { AlbumRepositoryImpl(get()) }
}
