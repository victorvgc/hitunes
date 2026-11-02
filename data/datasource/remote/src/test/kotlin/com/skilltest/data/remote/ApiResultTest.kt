package com.skilltest.data.remote

import com.google.gson.JsonSyntaxException
import com.skilltest.domain.model.ResultWrapper
import java.io.IOException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ApiResultTest {
    @Test
    fun successTransformsValueExactlyOnce() {
        var calls = 0
        val result = ApiResult.Success(5).toResult { value ->
            calls++
            value.toString()
        }

        assertEquals("5", result.data)
        assertEquals(1, calls)
    }

    @Test
    fun failuresDoNotTransformAndKeepHttpCode() {
        var calls = 0
        val failure: ApiResult<String> = ApiResult.Failure(ApiError.Http(404, "missing"))
        val result = failure.toResult {
            calls++
            it
        }

        assertTrue(result is ResultWrapper.Error)
        assertEquals(404, result.error?.code)
        assertEquals(0, calls)
    }

    @Test
    fun classifiesNetworkSerializationAndUnknownFailures() {
        assertTrue(IOException("offline").toApiError() is ApiError.Network)
        assertTrue(JsonSyntaxException("invalid").toApiError() is ApiError.Serialization)
        assertTrue(IllegalStateException("broken").toApiError() is ApiError.Unknown)
        assertEquals("Network failure", ApiError.Network(IOException()).toFailure().msg)
        assertEquals("Serialization", ApiError.Serialization(JsonSyntaxException("bad")).toFailure().msg)
        assertEquals("Unknown", ApiError.Unknown(IllegalStateException()).toFailure().msg)
    }

    @Test
    fun classifiesHttpExceptionWithCodeAndErrorBody() {
        val response = Response.error<String>(
            429,
            "rate limited".toResponseBody("text/plain".toMediaType()),
        )

        val error = HttpException(response).toApiError() as ApiError.Http

        assertEquals(429, error.code)
        assertEquals("rate limited", error.body)
    }

    @Test
    fun safeCallReturnsSuccessAndMapsThrownIoException() = runTest {
        val success = safeCall { "song" }
        val failure = safeCall<String> { throw IOException("offline") }

        assertEquals("song", (success as ApiResult.Success).value)
        assertTrue((failure as ApiResult.Failure).error is ApiError.Network)
    }
}
