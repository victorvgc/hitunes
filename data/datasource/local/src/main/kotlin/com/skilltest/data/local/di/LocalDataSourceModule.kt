package com.skilltest.data.local.di

import com.skilltest.data.datasource.SongsListDataSource
import com.skilltest.data.local.LocalSongsListDataSource
import org.koin.dsl.module

val localDataSourceModule = module {
    single<SongsListDataSource.Local> { LocalSongsListDataSource() }
}
