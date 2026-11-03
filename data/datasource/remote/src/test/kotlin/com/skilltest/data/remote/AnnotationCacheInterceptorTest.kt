package com.skilltest.data.remote

import java.lang.reflect.Proxy
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import retrofit2.Invocation

class AnnotationCacheInterceptorTest {
    @Test
    fun successfulAnnotatedGetAddsCacheHeaderAndRemovesPragma() {
        val response = intercept(requestFor("cached"))

        assertEquals("max-age=60", response.header("Cache-Control"))
        assertNull(response.header("Pragma"))
    }

    @Test
    fun unannotatedGetKeepsOriginalHeaders() {
        val response = intercept(requestFor("plain"))

        assertEquals("no-cache", response.header("Cache-Control"))
        assertEquals("no-cache", response.header("Pragma"))
    }

    @Test
    fun failedRequestDoesNotAddCacheHeader() {
        val response = intercept(requestFor("cached"), code = 500)

        assertEquals("no-cache", response.header("Cache-Control"))
    }

    @Test
    fun postRequestDoesNotAddCacheHeader() {
        val request = requestFor("cached").newBuilder().post("body".toRequestBody()).build()
        val response = intercept(request)

        assertEquals("no-cache", response.header("Cache-Control"))
    }

    private fun requestFor(methodName: String): Request {
        val method = FakeService::class.java.getMethod(methodName)
        return Request.Builder()
            .url("https://example.test/search")
            .tag(Invocation::class.java, Invocation.of(method, emptyList<Any>()))
            .build()
    }

    private fun intercept(request: Request, code: Int = 200): Response {
        val downstream = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message("response")
            .header("Cache-Control", "no-cache")
            .header("Pragma", "no-cache")
            .build()
        val chain = Proxy.newProxyInstance(
            Interceptor.Chain::class.java.classLoader,
            arrayOf(Interceptor.Chain::class.java),
        ) { _, method, _ ->
            when (method.name) {
                "request" -> request
                "proceed" -> downstream
                else -> error("Unexpected chain method: ${method.name}")
            }
        } as Interceptor.Chain

        return AnnotationCacheInterceptor().intercept(chain)
    }

    private interface FakeService {
        @Cached(maxAgeInSeconds = 60)
        fun cached()
        fun plain()
    }
}
