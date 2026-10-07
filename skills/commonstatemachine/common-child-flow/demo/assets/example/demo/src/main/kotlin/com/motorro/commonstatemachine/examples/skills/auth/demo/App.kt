package com.motorro.commonstatemachine.examples.skills.auth.demo

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.plugin.module.dsl.startKoin

@KoinApplication(modules = [AppModule::class])
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())

        startKoin<App> {
            allowOverride(false)
            androidLogger()
            androidContext(this@App)
        }
    }
}

