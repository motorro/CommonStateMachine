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

package com.motorro.statemachine.welcome.usecase

import com.motorro.statemachine.commonapi.NETWORK_DELAY
import com.motorro.statemachine.commonapi.welcome.data.BAD
import com.motorro.statemachine.commonapi.welcome.data.GOOD
import com.motorro.statemachine.commoncore.coroutines.DispatcherProvider
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Checks if user is registered
 */
@ViewModelScoped
class CheckEmail @Inject constructor(private val dispatchers: DispatcherProvider) {
    /**
     * Checks if user is registered
     * @param email User to check
     */
    suspend operator fun invoke(email: String): Boolean = withContext(dispatchers.default) {
        val exists = checkUserExists(email)
        withContext(dispatchers.main) { exists }
    }

    private suspend fun checkUserExists(email: String): Boolean {
        delay(NETWORK_DELAY)
        return REGISTERED_USERS.contains(email.trim().lowercase())
    }

    companion object {
        /**
         * Registered users which switch to login flow
         */
        private val REGISTERED_USERS = setOf(
            GOOD,
            BAD
        )
    }
}