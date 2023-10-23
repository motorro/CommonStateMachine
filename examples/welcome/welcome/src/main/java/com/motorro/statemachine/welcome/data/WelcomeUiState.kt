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

package com.motorro.statemachine.welcome.data

import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.register.data.RegisterUiState

/**
 * Welcome flow UI state
 */
sealed class WelcomeUiState {

    /**
     * Loading data...
     */
    object Loading : WelcomeUiState()

    /**
     * Welcome page
     * @property message Welcome message
     * @property termsAccepted Accept terms and conditions switch state
     * @property actionEnabled Action (next) button state
     */
    data class Welcome(
        val message: String,
        val termsAccepted: Boolean,
        val actionEnabled: Boolean
    ) : WelcomeUiState()

    /**
     * Email entry screen
     * @property email Email field value
     * @property actionEnabled Action (next) button state
     */
    data class EmailEntry(
        val email: String,
        val actionEnabled: Boolean
    ) : WelcomeUiState()

    /**
     * Login state wrapper
     * @property value Login UI state
     */
    data class Login(val value: LoginUiState) : WelcomeUiState()

    /**
     * Register state wrapper
     * @property value Register UI state
     */
    data class Register(val value: RegisterUiState) : WelcomeUiState()

    /**
     * Registration complete screen
     * @property email Registered user's email
     */
    data class Complete(val email: String) : WelcomeUiState()

    /**
     * Registration wizard has terminated
     */
    object Terminated : WelcomeUiState()
}