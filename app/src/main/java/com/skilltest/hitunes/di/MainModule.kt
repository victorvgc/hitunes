package com.skilltest.hitunes.di

import com.skilltest.data.local.di.localDataSourceModule
import com.skilltest.data.remote.di.remoteDataSourceModule
import com.skilltest.data.remote.provideRetrofitWithCache
import com.skilltest.data.repository.di.repositoryModule
import com.skilltest.hitunes.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val mainModule = module {
    includes(remoteDataSourceModule, localDataSourceModule, repositoryModule)

    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
    }

    single {
        provideRetrofitWithCache(get(), get())
    }
}
