package com.skilltest.domain.usecase

import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.Track
import com.skilltest.domain.repository.SongRepository

class AddToRecentlyPlayedUseCase(
    private val repository: SongRepository,
) : BaseUseCase<AddToRecentlyPlayedUseCase.Request, Unit> {
    override suspend fun invoke(request: Request): ResultWrapper<Unit> =
        repository.addToRecentlyPlayed(request.track)

    data class Request(
        val track: Track,
    )
}
