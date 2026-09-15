package com.motorro.statemachine.skills.auth.implementation.data

import com.motorro.commonstatemachine.skills.domain.entity.PasswordRequirements
import com.motorro.commonstatemachine.skills.domain.exception.IOException
import com.motorro.commonstatemachine.skills.domain.exception.UnknownException
import com.motorro.statemachine.skills.auth.api.AuthInput
import com.motorro.statemachine.skills.auth.api.AuthResult

/**
 * Input fixture
 */
internal val INPUT = AuthInput("Please login!")

/**
 * Result fixture
 */
internal val RESULT = AuthResult(authenticated = true)

internal val PASSWORD_REQUIREMENTS = PasswordRequirements(
    regex = "^.{8,}$".toRegex(),
    description = "Minimum eight characters"
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

/**
 * Fatal error fixture
 */
internal val FATAL_ERROR = UnknownException("Something went wrong")

/**
 * Non-fatal error fixture
 */
internal val NON_FATAL_ERROR = IOException("Internet is down")