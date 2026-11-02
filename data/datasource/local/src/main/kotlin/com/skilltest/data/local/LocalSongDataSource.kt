package com.skilltest.data.local

import com.skilltest.data.datasource.SongDataSource
import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.Track

internal class LocalSongDataSource : SongDataSource.Local {
    override suspend fun fetchRecentlyPlayedSongs(): ResultWrapper<RecentlyPlayed> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRecentlyPlayedSong(track: Track): ResultWrapper<Unit> {
        TODO("Not yet implemented")
    }
}
