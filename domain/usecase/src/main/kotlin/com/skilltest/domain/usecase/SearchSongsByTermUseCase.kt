package com.skilltest.domain.usecase

import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.repository.SongRepository

class SearchSongsByTermUseCase(
    private val repository: SongRepository,
) : BaseUseCase<SearchSongsByTermUseCase.Request, SearchResult> {

    override suspend fun invoke(request: Request): ResultWrapper<SearchResult> =
        repository.searchSongsByTerm(term = request.term)

    data class Request(
        val term: String
    )
}
