package com.skilltest.data.remote

import com.skilltest.data.remote.datasource.RemoteAlbumDataSource
import com.skilltest.data.remote.model.RemoteAlbum
import com.skilltest.data.remote.model.RemoteEntity
import com.skilltest.data.remote.service.AlbumService
import com.skilltest.domain.model.ResultWrapper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteAlbumDataSourceTest {
    @Test
    fun requestsAlbumAndMapsNullableResponse() = runTest {
        var requestedId: String? = null
        val service = object : AlbumService {
            override suspend fun fetchAlbumSongs(albumId: String): RemoteAlbum {
                requestedId = albumId
                return RemoteAlbum(
                    results = listOf(RemoteEntity(wrapperType = "collection", collectionId = 9))
                )
            }
        }

        val result = RemoteAlbumDataSource(service).loadAlbum("9")

        assertEquals("9", requestedId)
        assertTrue(result is ResultWrapper.Success)
        assertEquals("9", result.data?.id)
        assertEquals("", result.data?.name)
    }
}
