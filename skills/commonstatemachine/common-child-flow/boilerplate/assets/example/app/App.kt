package com.motorro.commonstatemachine.examples.skills.app

import android.app.Application
import com.motorro.commonstatemachine.examples.skills.auth.implementation.AuthModule
import com.motorro.commonstatemachine.skills.usecase.UsecaseModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.plugin.module.dsl.startKoin

@KoinApplication(modules = [
    UsecaseModule::class,
    AuthModule::class,
    MainModule::class
])
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
