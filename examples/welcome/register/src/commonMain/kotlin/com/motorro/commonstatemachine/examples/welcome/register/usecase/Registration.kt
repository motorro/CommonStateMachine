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

package com.motorro.commonstatemachine.examples.welcome.register.usecase

import com.motorro.commonstatemachine.examples.commoncore.coroutines.DispatcherProvider
import com.motorro.commonstatemachine.examples.welcome.commonapi.NETWORK_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

/**
 * Emulates user registration
 */
internal interface Registration {
    /**
     * Registers user
     * @param email Email to register
     * @param password Password to set
     */
    suspend operator fun invoke(email: String, password: String): Boolean

    @Factory
    class Impl(private val dispatchers: DispatcherProvider): Registration {
        /**
         * Registers user
         * @param email Email to register
         * @param password Password to set
         */
        override suspend operator fun invoke(email: String, password: String): Boolean = withContext(dispatchers.default) {
            delay(NETWORK_DELAY)
            withContext(dispatchers.main) { true }
        }
    }
}
