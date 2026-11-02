package com.skilltest.data.remote.service

import com.skilltest.data.remote.Cached
import com.skilltest.data.remote.model.RemoteAlbum
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumService {

    @Cached
    @GET("lookup?entity=song")
    suspend fun fetchAlbumSongs(@Query("id") albumId: String): RemoteAlbum
}
