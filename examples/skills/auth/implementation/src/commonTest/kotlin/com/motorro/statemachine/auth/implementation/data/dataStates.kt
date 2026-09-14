package com.motorro.statemachine.auth.implementation.data

import com.motorro.statemachine.auth.api.AuthInput
import com.motorro.statemachine.auth.api.AuthResult

/**
 * Input fixture
 */
internal val INPUT = AuthInput("Please login!")

/**
 * Result fixture
 */
internal val RESULT = AuthResult(authenticated = true)

internal val PASSWORD_REQUIREMENTS = PasswordRequirements(
    regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}\$".toRegex(),
    description = "Minimum eight characters, at least one letter and one number"
)

/**
 * Initial state
 */
internal val EMPTY_STATE = AuthStateData(INPUT, PASSWORD_REQUIREMENTS)

/**
 * Valid form state
 */
internal val VALID_FORM_STATE = EMPTY_STATE.copy(
    username = "user",
    password = "password"
)