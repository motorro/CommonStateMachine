/*
 * Copyright 2026 Nikolai Kotchetkov.
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

package com.motorro.commonstatemachine.examples.skills.auth.journey

import com.motorro.commonstatemachine.examples.skills.auth.implementation.AuthModule
import com.motorro.commonstatemachine.skills.domain.authenticate.AuthenticateWithPassword
import com.motorro.commonstatemachine.skills.domain.authenticate.GetPasswordRequirements
import com.motorro.commonstatemachine.skills.domain.authenticate.data.PasswordRequirements
import com.motorro.commonstatemachine.skills.domain.exception.AuthenticationException
import com.motorro.commonstatemachine.skills.domain.session.data.Username
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds

const val USERNAME = "user"
const val PASSWORD = "password"

/**
 * Demo fixtures for Happy Path flavor
 */
@Module(includes = [AuthModule::class])
@ComponentScan
class AppModule {

    @Single
    fun getPasswordRequirements(): GetPasswordRequirements {
        val tag = GetPasswordRequirements::class.simpleName!!
        return object : GetPasswordRequirements {
            override suspend fun invoke(): PasswordRequirements {
                delay(2.seconds)
                Napier.d(tag = tag) { "Emulating success" }
                return PasswordRequirements(
                    regex = "^.{8,}$".toRegex(),
                    description = "Minimum eight characters"
                )
            }
        }
    }

    @Single
    fun authenticateWithPassword(): AuthenticateWithPassword {
        val tag = AuthenticateWithPassword::class.simpleName!!
        return object : AuthenticateWithPassword {
            override suspend fun invoke(username: Username, password: String) {
                delay(2.seconds)
                if (USERNAME != username.value || PASSWORD != password) {
                    Napier.d(tag = tag) { "Emulating failure" }
                    throw AuthenticationException("Invalid username or password")
                }
                Napier.d(tag = tag) { "Emulating success" }
            }
        }
    }
}
