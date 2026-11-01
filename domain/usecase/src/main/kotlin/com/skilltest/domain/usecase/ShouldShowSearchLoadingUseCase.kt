package com.skilltest.domain.usecase

import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.toSuccess

class ShouldShowSearchLoadingUseCase :
    BaseUseCase<ShouldShowSearchLoadingUseCase.Request, Boolean> {
    override suspend fun invoke(request: Request): ResultWrapper<Boolean> =
        (request.term.length > 2).toSuccess()

    data class Request(
        val term: String,
    )
}
