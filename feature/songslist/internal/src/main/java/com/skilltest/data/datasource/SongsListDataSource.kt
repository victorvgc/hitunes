package com.skilltest.data.datasource

import com.skilltest.core.domain.model.ResultWrapper
import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track

sealed interface SongsListDataSource {
    interface Remote : SongsListDataSource {
        suspend fun fetchSearchedSongs(term: String): ResultWrapper<SearchResult>
    }

    interface Local : SongsListDataSource {
        suspend fun fetchRecentlyPlayedSongs(): ResultWrapper<RecentlyPlayed>
        suspend fun saveRecentlyPlayedSong(track: Track): ResultWrapper<Unit>
    }
}
