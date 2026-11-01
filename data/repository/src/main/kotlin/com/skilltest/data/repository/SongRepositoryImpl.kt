package com.skilltest.data.repository

import com.skilltest.data.datasource.SongsListDataSource
import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track
import com.skilltest.domain.repository.SongRepository

internal class SongRepositoryImpl(
    private val remote: SongsListDataSource.Remote,
    private val local: SongsListDataSource.Local,
) : SongRepository {
    override suspend fun searchSongsByTerm(term: String): ResultWrapper<SearchResult> =
        remote.fetchSearchedSongs(term)

    override suspend fun addToRecentlyPlayed(track: Track): ResultWrapper<Unit> =
        local.saveRecentlyPlayedSong(track)

    override suspend fun getRecentlyPlayed(): ResultWrapper<RecentlyPlayed> =
        local.fetchRecentlyPlayedSongs()
}
