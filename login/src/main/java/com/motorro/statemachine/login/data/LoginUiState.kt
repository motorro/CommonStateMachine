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
     * @property email Login email
     * @property password Password field value
     * @property message Error message
     */
    data class LoginError(
        val email: String,
        val password: String,
        val message: String
    ) : LoginUiState()
}