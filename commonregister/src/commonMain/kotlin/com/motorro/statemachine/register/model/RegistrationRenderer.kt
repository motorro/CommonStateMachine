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

package com.motorro.statemachine.register.model

import com.motorro.statemachine.commoncore.resources.ResourceWrapper
import com.motorro.statemachine.register.data.PasswordValidationError
import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.data.RegisterUiState

/**
 * Renders data state
 * Externalizes resource usage
 */
interface RegistrationRenderer {
    /**
     * Renders password entry
     */
    fun renderPasswordEntry(
        data: RegisterDataState,
        repeatPassword: String?,
        error: PasswordValidationError?
    ): RegisterUiState

    /**
     * Renders registration network operation
     */
    fun renderRegistration(data: RegisterDataState): RegisterUiState

    class Impl(resourceWrapper: ResourceWrapper): RegistrationRenderer, ResourceWrapper by resourceWrapper {
        /**
         * Renders password entry
         */
        override fun renderPasswordEntry(
            data: RegisterDataState,
            repeatPassword: String?,
            error: PasswordValidationError?
        ): RegisterUiState = RegisterUiState.PasswordEntry(
            data.commonData.email.orEmpty(),
            data.password.orEmpty(),
            repeatPassword.orEmpty(),
            error?.let(::getPasswordError)
        )

        /**
         * Renders registration network operation
         */
        override fun renderRegistration(data: RegisterDataState): RegisterUiState = RegisterUiState.Loading
    }
}

/**
 * Returns password error text
 */
expect fun ResourceWrapper.getPasswordError(error: PasswordValidationError): String
