/*
 * Copyright 2022 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorro.commonstatemachine.examples.welcome.app

import android.app.Application
import com.motorro.commonstatemachine.examples.welcome.login.LoginModule
import com.motorro.commonstatemachine.examples.welcome.register.RegisterModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinApplication
import org.koin.plugin.module.dsl.startKoin

@KoinApplication(modules = [
    RegisterModule::class,
    LoginModule::class,
    WelcomeModule::class,
])
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupLogger()
        startKoin<App> {
            allowOverride(false)
            androidLogger()
            androidContext(this@App)
        }
        Napier.d { "Started" }
    }

    private fun setupLogger() {
        Napier.base(DebugAntilog())
    }
}
