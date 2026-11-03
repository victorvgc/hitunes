package com.skilltest.data.local.di

import androidx.room.Room
import com.skilltest.data.datasource.SongDataSource
import com.skilltest.data.local.LocalSongDataSource
import com.skilltest.data.local.database.HiTunesDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataSourceModule = module {
    single {
        Room.databaseBuilder(androidContext(), HiTunesDatabase::class.java, HiTunesDatabase.NAME)
            .build()
    }
    single { get<HiTunesDatabase>().recentlyPlayedDao() }
    single<SongDataSource.Local> { LocalSongDataSource(get()) }
}
