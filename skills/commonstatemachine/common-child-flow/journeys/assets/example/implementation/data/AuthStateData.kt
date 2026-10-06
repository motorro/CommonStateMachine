package com.motorro.commonstatemachine.examples.skills.auth.implementation.data

import com.motorro.commonstatemachine.examples.skills.auth.api.AuthInput
import com.motorro.commonstatemachine.skills.domain.authenticate.data.PasswordRequirements

/**
 * Interstate data to pass between the machine states
 * @property input Flow input from the flow initialization
 * @property passwordRequirements Password requirements (preloaded later)
 * @property username User input for the username is stored here
 * @property password User input for the password is stored here
 */
internal data class AuthStateData(
    val input: AuthInput,
    val passwordRequirements: PasswordRequirements,
    val username: String = "",
    val password: String = ""
)

/**
 * Some utility function to check the form state
 */
internal fun AuthStateData.isValidToAuthenticate() = username.isNotBlank() && passwordRequirements.regex.matches(password)
