package com.motorro.statemachine.registration.data

import com.motorro.statemachine.login.data.LoginUiState

/**
 * Registration flow UI state
 */
sealed class RegistrationUiState {

    /**
     * Loading data...
     */
    object Loading : RegistrationUiState()

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
    ) : RegistrationUiState()

    /**
     * Email entry screen
     * @property email Email field value
     * @property actionEnabled Action (next) button state
     */
    data class EmailEntry(
        val email: String,
        val actionEnabled: Boolean
    ) : RegistrationUiState()

    /**
     * Login state wrapper
     * @property value Login UI state
     */
    data class Login(val value: LoginUiState) : RegistrationUiState()

    /**
     * Password entry for login flow
     * @property password Password field value
     * @property repeatPassword Repeat password field value
     * @property actionEnabled Action (next) button state
     */
    data class RegistrationPasswordEntry(
        val password: String,
        val repeatPassword: String,
        val actionEnabled: Boolean
    ) : RegistrationUiState()

    /**
     * Registration complete screen
     * @property email Registered user's email
     */
    data class Complete(val email: String) : RegistrationUiState()

    /**
     * Registration wizard has terminated
     */
    object Terminated : RegistrationUiState()
}