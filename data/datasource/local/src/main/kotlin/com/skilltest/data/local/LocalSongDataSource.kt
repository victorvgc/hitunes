package com.skilltest.data.local

import com.skilltest.data.datasource.SongDataSource
import com.skilltest.data.local.database.RecentlyPlayedDao
import com.skilltest.data.local.model.RecentlyPlayedTrackEntity
import com.skilltest.data.local.model.RecentlyPlayedTrackEntity.Companion.toEntity
import com.skilltest.domain.model.Failure
import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.Track
import com.skilltest.domain.model.toSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber

internal class LocalSongDataSource(
    private val dao: RecentlyPlayedDao,
    private val currentTimeMillis: () -> Long = System::currentTimeMillis,
) : SongDataSource.Local {
    override fun fetchRecentlyPlayedSongs(): Flow<ResultWrapper<RecentlyPlayed>> =
        dao.getAllByMostRecent()
            .map<List<RecentlyPlayedTrackEntity>, ResultWrapper<RecentlyPlayed>> { rows ->
                RecentlyPlayed(rows.map { it.toDomain() }).toSuccess()
            }
            .catch { error ->
                if (error is CancellationException) throw error
                Timber.e(error, "Database observation error")
                emit(
                    ResultWrapper.Error(
                        Failure(
                            msg = error.message,
                            exception = error as? Exception
                        )
                    )
                )
            }

    override suspend fun saveRecentlyPlayedSong(track: Track): ResultWrapper<Unit> =
        safeDbCall {
            dao.upsert(track.toEntity(lastPlayed = currentTimeMillis()))
        }

    private suspend fun <T> safeDbCall(block: suspend () -> T): ResultWrapper<T> =
        try {
            block().toSuccess()
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Timber.e(e, "Database call error")
            ResultWrapper.Error(Failure(msg = e.message, exception = e))
        }
}
