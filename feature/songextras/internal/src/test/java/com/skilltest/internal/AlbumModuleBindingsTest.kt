package com.skilltest.internal

import com.skilltest.domain.model.Album
import com.skilltest.domain.model.ResultWrapper
import com.skilltest.domain.repository.AlbumRepository
import com.skilltest.domain.usecase.di.useCaseModule
import com.skilltest.internal.ui.di.albumModule
import com.skilltest.internal.ui.viewmodel.AlbumReducer
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class AlbumModuleBindingsTest {
    @Test
    fun reducerResolvesThroughAlbumUseCase() {
        val application = koinApplication {
            modules(
                albumModule,
                useCaseModule,
                module {
                    single<AlbumRepository> {
                        object : AlbumRepository {
                            override suspend fun loadAlbum(albumId: String): ResultWrapper<Album> =
                                ResultWrapper.Success(Album("", "", "", false, "", emptyList()))
                        }
                    }
                },
            )
        }

        try {
            assertNotNull(application.koin.get<AlbumReducer>())
        } finally {
            application.close()
        }
    }
}
