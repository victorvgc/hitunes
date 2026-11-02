package com.skilltest.domain.repository

import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track

interface SongRepository {
    suspend fun searchSongsByTerm(term: String): ResultWrapper<SearchResult>

    suspend fun addToRecentlyPlayed(track: Track): ResultWrapper<Unit>

    suspend fun getRecentlyPlayed(): ResultWrapper<RecentlyPlayed>
}
