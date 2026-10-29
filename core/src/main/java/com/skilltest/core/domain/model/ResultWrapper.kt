package com.skilltest.core.domain.model

sealed class ResultWrapper<T>(
    val data: T? = null,
    val error: Failure? = null,
) {
    data class Success<T>(
        val value: T
    ) : ResultWrapper<T>(data = value)

    data class Error<T>(
        val failure: Failure,
        val value: T? = null
    ) : ResultWrapper<T>(
        data = value,
        error = failure
    )

    suspend fun onSuccess(block: suspend (T) -> Unit) {
        if (this is Success) {
            block(this.value)
        }
    }

    suspend fun onError(block: suspend (Failure, T?) -> Unit) {
        if (this is Error) {
            block(this.failure, this.data)
        }
    }
}
