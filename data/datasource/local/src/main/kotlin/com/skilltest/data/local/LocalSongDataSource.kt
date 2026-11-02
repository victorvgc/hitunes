package com.skilltest.data.local

import com.skilltest.data.datasource.SongDataSource
import com.skilltest.data.local.database.RecentlyPlayedDao
import com.skilltest.data.local.model.RecentlyPlayedTrackEntity.Companion.toEntity
import com.skilltest.domain.model.Failure
import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.Track
import com.skilltest.domain.model.toSuccess
import timber.log.Timber

internal class LocalSongDataSource(
    private val dao: RecentlyPlayedDao,
    private val currentTimeMillis: () -> Long = System::currentTimeMillis,
) : SongDataSource.Local {
    override suspend fun fetchRecentlyPlayedSongs(): ResultWrapper<RecentlyPlayed> =
        safeDbCall {
            RecentlyPlayed(dao.getAllByMostRecent().map { it.toDomain() })
        }

    override suspend fun saveRecentlyPlayedSong(track: Track): ResultWrapper<Unit> =
        safeDbCall {
            dao.upsert(track.toEntity(lastPlayed = currentTimeMillis()))
        }

    private suspend fun <T> safeDbCall(block: suspend () -> T): ResultWrapper<T> =
        try {
            block().toSuccess()
        } catch (e: Exception) {
            Timber.e(e, "Database call error")
            ResultWrapper.Error(Failure(msg = e.message, exception = e))
        }
}
