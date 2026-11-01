package com.skilltest.domain.model

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ResultWrapperTest {
    @Test
    fun successExposesValueAndCallsOnlySuccessHandler() = runTest {
        val result = "song".toSuccess()
        var received: String? = null
        var errorCalls = 0

        result.onSuccess { received = it }
        result.onError { _, _ -> errorCalls++ }

        assertEquals("song", result.data)
        assertNull(result.error)
        assertEquals("song", received)
        assertEquals(0, errorCalls)
    }

    @Test
    fun errorExposesFailureAndOptionalValueAndCallsOnlyErrorHandler() = runTest {
        val failure = Failure(msg = "offline", code = 503)
        val result = ResultWrapper.Error(failure, "cached song")
        var successCalls = 0
        var receivedFailure: Failure? = null
        var receivedValue: String? = null

        result.onSuccess { successCalls++ }
        result.onError { error, value ->
            receivedFailure = error
            receivedValue = value
        }

        assertEquals("cached song", result.data)
        assertEquals(failure, result.error)
        assertEquals(failure, receivedFailure)
        assertEquals("cached song", receivedValue)
        assertEquals(0, successCalls)
    }
}
