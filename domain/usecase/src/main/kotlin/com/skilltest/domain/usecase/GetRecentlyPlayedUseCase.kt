package com.skilltest.domain.usecase

import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow

class GetRecentlyPlayedUseCase(
    private val repository: SongRepository,
) : BaseFlowUseCase<Unit, RecentlyPlayed> {
    override fun invoke(request: Unit): Flow<ResultWrapper<RecentlyPlayed>> =
        repository.getRecentlyPlayed()
}
