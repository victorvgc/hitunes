package com.skilltest.data.datasource

import com.skilltest.domain.model.Album
import com.skilltest.domain.model.ResultWrapper

sealed interface AlbumDataSource {
    interface Remote : AlbumDataSource {
        suspend fun loadAlbum(albumId: String): ResultWrapper<Album>
    }
}
