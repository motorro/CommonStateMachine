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

package com.motorro.commonstatemachine.examples.welcome.app.usecase

import com.motorro.commonstatemachine.examples.commoncore.coroutines.DispatcherProvider
import com.motorro.commonstatemachine.examples.commoncore.resources.ResourceWrapper
import com.motorro.commonstatemachine.examples.welcome.app.R
import com.motorro.commonstatemachine.examples.welcome.commonapi.NETWORK_DELAY
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

/**
 * Emulates data preload
 */
@Factory
class Preload(
    resourceWrapper: ResourceWrapper,
    private val dispatchers: DispatcherProvider,
): ResourceWrapper by resourceWrapper {
    /**
     * 'Loads' welcome greeting
     */
    suspend operator fun invoke(): String = withContext(dispatchers.default) {
        val greeting = loadGreeting()
        withContext(dispatchers.main) { greeting }
    }

    private suspend fun loadGreeting(): String {
        delay(NETWORK_DELAY)
        Napier.d("Loading complete")
        return getString(R.string.welcome_terms)
    }
}