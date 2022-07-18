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
