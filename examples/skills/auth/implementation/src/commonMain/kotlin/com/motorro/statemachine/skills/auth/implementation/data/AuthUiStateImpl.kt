package com.motorro.statemachine.skills.auth.implementation.data

import com.motorro.statemachine.skills.auth.api.AuthUiState

/**
 * All gestures available in this feature
 */
internal sealed class AuthUiStateImpl : AuthUiState {
    /**
     * Loading data
     */
    data object Loading : AuthUiStateImpl()

    /**
     * Login form
     * @property username Username
     * @property password Password
     * @property passwordRequirements Password requirements description
     * @property loginEnabled If true, the form is complete and the user can log in
     * @property canSkip If true, the user can skip the authentication
     */
    data class Form(
        val username: String,
        val password: String,
        val passwordRequirements: String,
        val loginEnabled: Boolean,
        val canSkip: Boolean
    ) : AuthUiStateImpl()

    /**
     * Displays error
     * @property message Error message to display
     * @property canRetry If true, the error is recoverable and can be retried
     */
    data class Error(val message: String, val canRetry: Boolean) : AuthUiStateImpl()
}