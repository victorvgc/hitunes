package com.skilltest.data.datasource

import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track
import kotlinx.coroutines.flow.Flow

sealed interface SongDataSource {
    interface Remote : SongDataSource {
        suspend fun fetchSearchedSongs(term: String): ResultWrapper<SearchResult>
    }

    interface Local : SongDataSource {
        fun fetchRecentlyPlayedSongs(): Flow<ResultWrapper<RecentlyPlayed>>
        suspend fun saveRecentlyPlayedSong(track: Track): ResultWrapper<Unit>
    }
}
