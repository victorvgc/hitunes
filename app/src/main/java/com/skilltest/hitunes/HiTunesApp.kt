package com.skilltest.hitunes

import android.app.Application
import com.skilltest.hitunes.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class HiTunesApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()

            androidContext(this@HiTunesApp)

            modules(appModules)
        }
    }
}
