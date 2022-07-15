package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.login.data.LoginUiState

/**
 * Common screen renderer example
 */
internal fun renderPassword(
    email: String,
    password: String,
    nextEnabled: Boolean
): LoginUiState.PasswordEntry = LoginUiState.PasswordEntry(
    email,
    password,
    nextEnabled
)
