package com.motorro.statemachine.auth.implementation.data

import com.motorro.statemachine.auth.api.AuthUiState

/**
 * All gestures available in this feature
 */
internal sealed class AuthUiStateImpl : AuthUiState {
    /**
     * Some common property implementation
     */
    override val interactive: Boolean = true

    /**
     * Loading data
     */
    data object Loading : AuthUiStateImpl()

    /**
     * Login form
     * @property username Username
     * @property password Password
     * @property loginEnabled If true, the form is complete and the user can log-in
     */
    data class Form(val username: String, val password: String, val loginEnabled: Boolean) : AuthUiStateImpl()

    /**
     * Displays error
     * @param message Error message to display
     */
    data class Error(val message: String) : AuthUiStateImpl()
}