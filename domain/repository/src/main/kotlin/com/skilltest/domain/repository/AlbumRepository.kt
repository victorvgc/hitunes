package com.skilltest.domain.repository

import com.skilltest.domain.model.Album
import com.skilltest.domain.model.ResultWrapper

interface AlbumRepository {
    suspend fun loadAlbum(albumId: String): ResultWrapper<Album>
}
