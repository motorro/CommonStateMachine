package com.motorro.statemachine

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupLogger()
    }

    private fun setupLogger() {
        Timber.plant(Timber.DebugTree())
    }
}