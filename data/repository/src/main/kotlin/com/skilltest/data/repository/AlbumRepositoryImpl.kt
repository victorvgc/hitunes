package com.skilltest.data.repository

import com.skilltest.data.datasource.AlbumDataSource
import com.skilltest.domain.model.Album
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.repository.AlbumRepository

internal class AlbumRepositoryImpl(
    private val remote: AlbumDataSource.Remote
) : AlbumRepository {
    override suspend fun loadAlbum(albumId: String): ResultWrapper<Album> =
        remote.loadAlbum(albumId)
}
