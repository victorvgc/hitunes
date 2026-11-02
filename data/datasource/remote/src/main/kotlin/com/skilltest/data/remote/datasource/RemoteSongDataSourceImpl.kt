package com.skilltest.data.remote.datasource

import com.skilltest.data.datasource.SongDataSource
import com.skilltest.data.remote.safeCall
import com.skilltest.data.remote.service.SongService
import com.skilltest.data.remote.toResult
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult

internal class RemoteSongDataSourceImpl(
    private val service: SongService
) : SongDataSource.Remote {
    override suspend fun fetchSearchedSongs(term: String): ResultWrapper<SearchResult> =
        safeCall { service.searchSongsByTerm(term) }.toResult { remote ->
            remote.toDomain()
        }
}
