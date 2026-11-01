package com.skilltest.data.repository.di

import com.skilltest.data.repository.SongsListRepositoryImpl
import com.skilltest.domain.repository.SongsListRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<SongsListRepository> { SongsListRepositoryImpl(get(), get()) }
}
