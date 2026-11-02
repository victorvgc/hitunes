package com.skilltest.domain.usecase

import com.skilltest.domain.model.ResultWrapper

interface BaseUseCase<Request, Result> {
    suspend operator fun invoke(request: Request): ResultWrapper<Result>
}
