package com.skilltest.domain.usecase

import com.skilltest.domain.model.Album
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.repository.AlbumRepository

class LoadAlbumDetailsUseCase(
    private val repository: AlbumRepository,
) : BaseUseCase<LoadAlbumDetailsUseCase.Request, Album> {
    override suspend fun invoke(request: Request): ResultWrapper<Album> =
        repository.loadAlbum(request.albumId)

    class Request(
        val albumId: String,
    )
}
