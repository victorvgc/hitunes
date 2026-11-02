package com.skilltest.domain.usecase

import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.repository.SongRepository

class GetRecentlyPlayedUseCase(
    private val repository: SongRepository,
) : BaseUseCase<Unit, RecentlyPlayed> {
    override suspend fun invoke(request: Unit): ResultWrapper<RecentlyPlayed> =
        repository.getRecentlyPlayed()
}
