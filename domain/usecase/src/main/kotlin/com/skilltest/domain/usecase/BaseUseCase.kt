package com.skilltest.domain.usecase

import com.skilltest.domain.model.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface BaseUseCase<Request, Result> {
    suspend operator fun invoke(request: Request): ResultWrapper<Result>
}

interface BaseFlowUseCase<Request, Result> {
    operator fun invoke(request: Request): Flow<ResultWrapper<Result>>
}
