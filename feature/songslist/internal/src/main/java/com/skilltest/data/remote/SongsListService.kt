package com.skilltest.data.remote

import com.skilltest.core.data.remote.Cached
import com.skilltest.data.remote.model.RemoteSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

interface SongsListService {

    @Cached
    @GET("search?media=music&entity=song")
    suspend fun searchSongsByTerm(
        @Query("term") term: String,
    ): RemoteSearchResult
}
