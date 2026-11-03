package com.skilltest.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.skilltest.data.local.model.RecentlyPlayedTrackEntity

@Database(
    entities = [RecentlyPlayedTrackEntity::class],
    version = 1,
)
internal abstract class HiTunesDatabase : RoomDatabase() {
    abstract fun recentlyPlayedDao(): RecentlyPlayedDao

    companion object {
        const val NAME = "hitunes.db"
    }
}
