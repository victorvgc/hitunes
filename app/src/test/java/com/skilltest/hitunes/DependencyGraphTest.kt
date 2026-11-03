package com.skilltest.hitunes

import android.app.Application
import com.skilltest.domain.repository.AlbumRepository
import com.skilltest.domain.repository.SongRepository
import com.skilltest.domain.usecase.AddToRecentlyPlayedUseCase
import com.skilltest.domain.usecase.GetRecentlyPlayedUseCase
import com.skilltest.domain.usecase.LoadAlbumDetailsUseCase
import com.skilltest.domain.usecase.SearchSongsByTermUseCase
import com.skilltest.hitunes.di.appModules
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import retrofit2.Retrofit

// A plain Application keeps HiTunesApp from starting the global Koin; Room needs a real Context.
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], application = Application::class)
class DependencyGraphTest {
    @Test
    fun `app resolves repository with remote and local datasource modules`() {
        val application = koinApplication {
            allowOverride(true)
            androidContext(RuntimeEnvironment.getApplication())
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
            assertNotNull(application.koin.get<GetRecentlyPlayedUseCase>())
            assertNotNull(application.koin.get<AddToRecentlyPlayedUseCase>())
        } finally {
            application.close()
        }
    }
}
