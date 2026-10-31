package com.skilltest.core.data.remote

import android.content.Context
import com.skilltest.core.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

private const val CACHE_SIZE: Long = 10 * 1024 * 1024
private const val CACHE_FILE_NAME = "http_cache"


fun provideRetrofitWithCache(context: Context, clientBuilder: OkHttpClient.Builder): Retrofit {

    val cacheDirectory = File(context.cacheDir, CACHE_FILE_NAME)
    val myCache = Cache(cacheDirectory, CACHE_SIZE)

    val okHttpClient = clientBuilder
        .cache(myCache)
        .addNetworkInterceptor(AnnotationCacheInterceptor())
        .build()

    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
