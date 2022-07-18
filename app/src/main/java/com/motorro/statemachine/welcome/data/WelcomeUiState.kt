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