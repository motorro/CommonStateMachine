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

package com.motorro.statemachine.login.data

/**
 * Login flow UI state
 */
sealed class LoginUiState {

    /**
     * Loading data...
     */
    object Loading : LoginUiState()

    /**
     * Password entry for login flow
     * @property email Login email
     * @property password Password field value
     * @property actionEnabled Action (next) button state
     */
    data class PasswordEntry(
        val email: String,
        val password: String,
        val actionEnabled: Boolean
    ) : LoginUiState()

    /**
     * Login error
     * @property passwordEntry Password screen data
     * @property message Error message
     */
    data class LoginError(
        val passwordEntry: PasswordEntry,
        val message: String
    ) : LoginUiState()
}