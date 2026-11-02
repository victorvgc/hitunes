package com.skilltest.data.remote.service

import com.skilltest.data.remote.Cached
import com.skilltest.data.remote.model.RemoteSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface SongService {

    @Cached
    @GET("search?media=music&entity=song")
    suspend fun searchSongsByTerm(
        @Query("term") term: String,
    ): RemoteSearchResult
}
