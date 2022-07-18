package com.motorro.statemachine.login.model

import com.motorro.statemachine.commoncore.resources.ResourceWrapper
import com.motorro.statemachine.login.R
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.login.di.LoginScope
import javax.inject.Inject

/**
 * Renders Login flow view states
 */
@LoginScope
class LoginRenderer @Inject constructor(resourceWrapper: ResourceWrapper): ResourceWrapper by resourceWrapper {
    /**
     * Renders password form
     */
    fun renderPassword(data: LoginDataState, isValid: Boolean): LoginUiState.PasswordEntry =
        LoginUiState.PasswordEntry(
            data.commonData.email.orEmpty(),
            data.password.orEmpty(),
            isValid
        )

    /**
     * Renders loading screen
     */
    fun renderLoading(data: LoginDataState): LoginUiState = LoginUiState.Loading

    /**
     * Renders password form
     */
    fun renderError(data: LoginDataState, error: Throwable): LoginUiState =
        LoginUiState.LoginError(
            renderPassword(data, false),
            getErrorMessage(error)
        )

    private fun getErrorMessage(error: Throwable): String = when(error) {
        is IllegalArgumentException -> getString(R.string.login_error_credentials)
        else -> getString(R.string.login_error_unknown)
    }
}

