package com.skilltest.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.skilltest.data.local.model.RecentlyPlayedTrackEntity

@Dao
internal interface RecentlyPlayedDao {
    @Upsert
    suspend fun upsert(track: RecentlyPlayedTrackEntity)

    @Query("SELECT * FROM recently_played_track ORDER BY last_played DESC")
    suspend fun getAllByMostRecent(): List<RecentlyPlayedTrackEntity>
}
