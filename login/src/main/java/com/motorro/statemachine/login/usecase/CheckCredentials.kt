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

package com.motorro.statemachine.login.usecase

import com.motorro.statemachine.commonapi.NETWORK_DELAY
import com.motorro.statemachine.commonapi.welcome.data.BAD
import com.motorro.statemachine.commonapi.welcome.data.GOOD
import com.motorro.statemachine.commoncore.coroutines.DispatcherProvider
import com.motorro.statemachine.login.di.LoginScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Emulates user's credentials check
 */
@LoginScope
class CheckCredentials @Inject constructor(private val dispatchers: DispatcherProvider) {
    /**
     * Tries to log-in user
     * @param email User to check
     * @param password Password to check
     */
    suspend operator fun invoke(email: String, password: String): Boolean = withContext(dispatchers.default) {
        val valid = checkCredentials(email)
        withContext(dispatchers.main) { valid }
    }

    private suspend fun checkCredentials(email: String): Boolean {
        delay(NETWORK_DELAY)
        return true == REGISTERED_USERS[email.trim().lowercase()]
    }

    companion object {
        /**
         * Users than can or can not login
         * Any password will do
         */
        private val REGISTERED_USERS = mapOf(
            BAD to false,
            GOOD to true
        )
    }
}