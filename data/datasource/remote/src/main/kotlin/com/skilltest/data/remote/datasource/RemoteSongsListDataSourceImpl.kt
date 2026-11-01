package com.skilltest.data.remote.datasource

import com.skilltest.data.datasource.SongsListDataSource
import com.skilltest.data.remote.SongsListService
import com.skilltest.data.remote.safeCall
import com.skilltest.data.remote.toResult
import com.skilltest.domain.model.ResultWrapper
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
