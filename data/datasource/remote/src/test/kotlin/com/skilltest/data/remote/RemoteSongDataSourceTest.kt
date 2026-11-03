package com.skilltest.data.remote

import com.skilltest.data.remote.datasource.RemoteSongDataSourceImpl
import com.skilltest.data.remote.model.RemoteSearchResult
import com.skilltest.data.remote.service.SongService
import com.skilltest.domain.model.ResultWrapper
import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteSongDataSourceTest {
    @Test
    fun forwardsTermAndMapsSuccessfulServiceResponse() = runTest {
        var requestedTerm: String? = null
        val service = object : SongService {
            override suspend fun searchSongsByTerm(term: String): RemoteSearchResult {
                requestedTerm = term
                return RemoteSearchResult(0, emptyList())
            }
        }

        val result = RemoteSongDataSourceImpl(service).fetchSearchedSongs("jazz")

        assertEquals("jazz", requestedTerm)
        assertTrue(result is ResultWrapper.Success)
        assertTrue(result.data!!.trackList.isEmpty())
    }

    @Test
    fun convertsServiceNetworkFailureToDomainFailure() = runTest {
        val service = object : SongService {
            override suspend fun searchSongsByTerm(term: String): RemoteSearchResult {
                throw IOException("offline")
            }
        }

        val result = RemoteSongDataSourceImpl(service).fetchSearchedSongs("jazz")

        assertTrue(result is ResultWrapper.Error)
        assertEquals("Network failure", result.error?.msg)
    }
}
