package com.skilltest.data.remote

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Cached(val maxAgeInSeconds: Int = 1800) // cache for 30min

internal class AnnotationCacheInterceptor : Interceptor {
    companion object {
        private const val HEADER_CACHE_CONTROL = "Cache-Control"
        private const val HEADER_PRAGMA = "Pragma"
        private const val HTTP_METHOD_GET = "GET"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val invocation = request.tag(Invocation::class.java)

        val cachedAnnotation = invocation?.method()?.getAnnotation(Cached::class.java)

        if (cachedAnnotation != null && request.method == HTTP_METHOD_GET && response.isSuccessful) {
            val cacheControl = CacheControl.Builder()
                .maxAge(cachedAnnotation.maxAgeInSeconds, java.util.concurrent.TimeUnit.SECONDS)
                .build()

            return response.newBuilder()
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .removeHeader(HEADER_PRAGMA)
                .build()
        }

        return response
    }
}
