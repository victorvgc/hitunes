package com.skilltest.data.remote

import timber.log.Timber

suspend inline fun <T> safeCall(
    block: suspend () -> T
): ApiResult<T> {
    return try {
        ApiResult.Success(block())
    } catch (e: Throwable) {
        Timber.e("Api call error -> ${e.toApiError()}")
        ApiResult.Failure(e.toApiError())
    }
}

fun Throwable.toApiError(): ApiError = when (this) {
    is retrofit2.HttpException -> {
        val body = response()?.errorBody()?.string()
        ApiError.Http(code(), body)
    }

    is java.io.IOException -> ApiError.Network(this)
    is com.google.gson.JsonSyntaxException,
    is com.google.gson.JsonIOException -> ApiError.Serialization(this)

    else -> ApiError.Unknown(this)
}
