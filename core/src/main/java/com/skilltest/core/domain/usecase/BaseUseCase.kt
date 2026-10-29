package com.skilltest.core.domain.usecase

import com.skilltest.core.domain.model.ResultWrapper

interface BaseUseCase<Request, Result> {
    suspend operator fun invoke(request: Request): ResultWrapper<Result>
}
