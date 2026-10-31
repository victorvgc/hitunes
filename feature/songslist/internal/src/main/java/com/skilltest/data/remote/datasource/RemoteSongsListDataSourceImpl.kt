package com.skilltest.data.remote.datasource

import com.skilltest.core.data.remote.safeCall
import com.skilltest.core.data.remote.toResult
import com.skilltest.core.domain.model.ResultWrapper
import com.skilltest.data.datasource.SongsListDataSource
import com.skilltest.data.remote.SongsListService
import com.skilltest.domain.model.SearchResult

internal class RemoteSongsListDataSourceImpl(
    private val service: SongsListService
) : SongsListDataSource.Remote {
    override suspend fun fetchSearchedSongs(term: String): ResultWrapper<SearchResult> {
        return safeCall { service.searchSongsByTerm(term) }.toResult { remote ->
            remote.toDomain()
        }
    }
}
