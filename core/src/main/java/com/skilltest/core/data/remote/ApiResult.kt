package com.skilltest.core.data.remote

import com.skilltest.core.domain.model.Failure
import com.skilltest.core.domain.model.ResultWrapper

sealed interface ApiResult<out T> {
    data class Success<T>(val value: T) : ApiResult<T>
    data class Failure(val error: ApiError) : ApiResult<Nothing>
}

sealed interface ApiError {
    data class Http(val code: Int, val body: String?) : ApiError
    data class Network(val cause: Throwable) : ApiError
    data class Serialization(val cause: Throwable) : ApiError
    data class Unknown(val cause: Throwable) : ApiError
}

data object Network : Failure(msg = "Network failure")

data class Http(
    override val code: Int
) : Failure(code = code)

data class Other(
    override val msg: String
) : Failure(msg = msg)

fun ApiError.toFailure(): Failure = when (this) {
    is ApiError.Http -> Http(this.code)
    is ApiError.Network -> Network
    is ApiError.Serialization -> Other("Serialization")
    is ApiError.Unknown -> Other("Unknown")
}

inline fun <reified T, reified R> ApiResult<T>.toResult(transform: (T) -> R): ResultWrapper<R> =
    when (this) {
        is ApiResult.Failure -> ResultWrapper.Error(failure = this.error.toFailure())
        is ApiResult.Success<T> -> ResultWrapper.Success(value = transform(this.value))
    }
