package com.motorro.statemachine.registration.data

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
     * Password entry for login flow
     * @property password Password field value
     * @property actionEnabled Action (next) button state
     */
    data class LoginPasswordEntry(
        val password: String,
        val actionEnabled: Boolean
    ) : RegistrationUiState()

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
}