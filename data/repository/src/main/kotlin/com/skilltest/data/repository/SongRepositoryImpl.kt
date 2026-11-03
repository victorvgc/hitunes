package com.skilltest.data.repository

import com.skilltest.data.datasource.SongDataSource
import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track
import com.skilltest.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow

internal class SongRepositoryImpl(
    private val remote: SongDataSource.Remote,
    private val local: SongDataSource.Local,
) : SongRepository {
    override suspend fun searchSongsByTerm(term: String): ResultWrapper<SearchResult> =
        remote.fetchSearchedSongs(term)

    override suspend fun addToRecentlyPlayed(track: Track): ResultWrapper<Unit> =
        local.saveRecentlyPlayedSong(track)

    override fun getRecentlyPlayed(): Flow<ResultWrapper<RecentlyPlayed>> =
        local.fetchRecentlyPlayedSongs()
}
