package com.skilltest.data.remote.datasource

import com.skilltest.data.datasource.AlbumDataSource
import com.skilltest.data.remote.safeCall
import com.skilltest.data.remote.service.AlbumService
import com.skilltest.data.remote.toResult
import com.skilltest.domain.model.Album
import com.skilltest.domain.model.ResultWrapper

internal class RemoteAlbumDataSource(
    private val service: AlbumService
) : AlbumDataSource.Remote {
    override suspend fun loadAlbum(albumId: String): ResultWrapper<Album> =
        safeCall { service.fetchAlbumSongs(albumId) }.toResult { it.toDomain() }
}
