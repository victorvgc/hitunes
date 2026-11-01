package com.skilltest.hitunes

import com.skilltest.domain.repository.SongsListRepository
import com.skilltest.hitunes.di.mainModule
import com.skilltest.shared.di.songsListModule
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
                mainModule,
                songsListModule,
                module {
                    // Avoid Android's cache directory and network access in this JVM test.
                    single<Retrofit> {
                        Retrofit.Builder().baseUrl("https://example.test/").build()
                    }
                },
            )
        }

        try {
            assertNotNull(application.koin.get<SongsListRepository>())
        } finally {
            application.close()
        }
    }
}
