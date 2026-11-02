package com.skilltest.hitunes

import com.skilltest.domain.repository.AlbumRepository
import com.skilltest.domain.repository.SongRepository
import com.skilltest.domain.usecase.LoadAlbumDetailsUseCase
import com.skilltest.domain.usecase.SearchSongsByTermUseCase
import com.skilltest.hitunes.di.appModules
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import retrofit2.Retrofit

class DependencyGraphTest {
    @Test
    fun `app resolves repository with remote and local datasource modules`() {
        val application = koinApplication {
            allowOverride(true)
            modules(
                appModules + module {
                    // Avoid Android's cache directory and network access in this JVM test.
                    single<Retrofit> {
                        Retrofit.Builder().baseUrl("https://example.test/").build()
                    }
                },
            )
        }

        try {
            assertNotNull(application.koin.get<SongRepository>())
            assertNotNull(application.koin.get<AlbumRepository>())
            assertNotNull(application.koin.get<SearchSongsByTermUseCase>())
            assertNotNull(application.koin.get<LoadAlbumDetailsUseCase>())
        } finally {
            application.close()
        }
    }
}
