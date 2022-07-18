package com.motorro.statemachine.welcome.data

import com.motorro.statemachine.login.data.LoginUiState

/**
 * Welcome flow UI state
 */
sealed class WelcomeUiState {

    /**
     * Loading data...
     */
    object Loading : com.motorro.statemachine.welcome.data.WelcomeUiState()

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
    ) : com.motorro.statemachine.welcome.data.WelcomeUiState()

    /**
     * Email entry screen
     * @property email Email field value
     * @property actionEnabled Action (next) button state
     */
    data class EmailEntry(
        val email: String,
        val actionEnabled: Boolean
    ) : com.motorro.statemachine.welcome.data.WelcomeUiState()

    /**
     * Login state wrapper
     * @property value Login UI state
     */
    data class Login(val value: LoginUiState) : com.motorro.statemachine.welcome.data.WelcomeUiState()

    /**
     * Registration complete screen
     * @property email Registered user's email
     */
    data class Complete(val email: String) : com.motorro.statemachine.welcome.data.WelcomeUiState()

    /**
     * Registration wizard has terminated
     */
    object Terminated : com.motorro.statemachine.welcome.data.WelcomeUiState()
}