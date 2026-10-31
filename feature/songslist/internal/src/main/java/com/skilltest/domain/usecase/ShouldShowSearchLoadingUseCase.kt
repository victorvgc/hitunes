package com.skilltest.domain.usecase

import com.skilltest.core.domain.model.ResultWrapper
import com.skilltest.core.domain.model.toSuccess
import com.skilltest.core.domain.usecase.BaseUseCase

class ShouldShowSearchLoadingUseCase :
    BaseUseCase<ShouldShowSearchLoadingUseCase.Request, Boolean> {
    override suspend fun invoke(request: Request): ResultWrapper<Boolean> =
        (request.term.length > 2).toSuccess()

    data class Request(
        val term: String,
    )
}
