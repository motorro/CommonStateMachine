package com.motorro.statemachine.skills.auth.demo

import android.app.Application
import com.motorro.commonstatemachine.skills.domain.authenticate.AuthenticateWithPassword
import com.motorro.commonstatemachine.skills.domain.authenticate.GetPasswordRequirements
import com.motorro.commonstatemachine.skills.domain.authenticate.data.PasswordRequirements
import com.motorro.commonstatemachine.skills.domain.exception.AuthenticationException
import com.motorro.commonstatemachine.skills.domain.exception.IOException
import com.motorro.commonstatemachine.skills.domain.session.data.Username
import com.motorro.statemachine.skills.auth.implementation.AuthModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.plugin.module.dsl.startKoin
import kotlin.time.Duration.Companion.seconds

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

@Module(includes = [AuthModule::class])
@ComponentScan
class AppModule {

    @Single
    fun getPasswordRequirements(): GetPasswordRequirements {
        var attempt = 1
        val tag = GetPasswordRequirements::class.simpleName!!
        return object : GetPasswordRequirements {
            override suspend fun invoke(): PasswordRequirements {
                delay(2.seconds)
                when (attempt) {
                    1 -> {
                        Napier.d(tag = tag) { "Emulating non-fatal error" }
                        attempt = 2
                        throw IOException("Network error")
                    }
                    else -> {
                        Napier.d(tag = tag) { "Emulating success" }
                        attempt = 1
                        return PasswordRequirements(
                            regex = "^.{8,}$".toRegex(),
                            description = "Minimum eight characters"
                        )
                    }
                }
            }
        }
    }

    @Single
    fun authenticateWithPassword(): AuthenticateWithPassword {
        var attempt = 1
        val tag = AuthenticateWithPassword::class.simpleName!!
        return object : AuthenticateWithPassword {
            override suspend fun invoke(username: Username, password: String) {
                delay(2.seconds)
                when (attempt) {
                    1 -> {
                        Napier.d(tag = tag) { "Emulating credentials error" }
                        attempt = 2
                        throw AuthenticationException("Invalid credentials")
                    }
                    2 -> {
                        Napier.d(tag = tag) { "Emulating non-fatal error" }
                        attempt = 3
                        throw IOException("Network error")
                    }
                    else -> {
                        Napier.d(tag = tag) { "Emulating success" }
                        attempt = 1
                    }
                }
            }
        }
    }
}
