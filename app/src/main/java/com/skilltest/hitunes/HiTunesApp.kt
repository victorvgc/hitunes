package com.skilltest.hitunes

import android.app.Application
import timber.log.Timber

class HiTunesApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
