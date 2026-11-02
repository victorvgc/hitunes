package com.skilltest.data.local.di

import com.skilltest.data.datasource.SongDataSource
import com.skilltest.data.local.LocalSongDataSource
import org.koin.dsl.module

val localDataSourceModule = module {
    single<SongDataSource.Local> { LocalSongDataSource() }
}
