package com.skilltest.ui

import com.skilltest.domain.model.RecentlyPlayed
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.model.SearchResult
import com.skilltest.domain.model.Track
import com.skilltest.domain.repository.SongRepository
import com.skilltest.domain.usecase.di.useCaseModule
import com.skilltest.ui.di.internalSongsListModule
import com.skilltest.ui.viewmodel.SongsListReducer
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class SongsListModuleBindingsTest {
    @Test
    fun reducerResolvesThroughSongUseCases() {
        val application = koinApplication {
            modules(
                internalSongsListModule,
                useCaseModule,
                module {
                    single<SongRepository> {
                        object : SongRepository {
                            override suspend fun searchSongsByTerm(term: String) =
                                ResultWrapper.Success(SearchResult(emptyList()))

                            override suspend fun addToRecentlyPlayed(track: Track) =
                                ResultWrapper.Success(Unit)

                            override fun getRecentlyPlayed() =
                                flowOf(ResultWrapper.Success(RecentlyPlayed(emptyList())))
                        }
                    }
                },
            )
        }

        try {
            assertNotNull(application.koin.get<SongsListReducer>())
        } finally {
            application.close()
        }
    }
}
