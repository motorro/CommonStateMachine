package com.motorro.statemachine.register.model

import com.motorro.statemachine.commoncore.resources.ResourceWrapper
import com.motorro.statemachine.register.data.PasswordValidationError
import com.motorro.statemachine.register.data.PasswordValidationError.PASSWORD_LENGTH
import com.motorro.statemachine.register.data.PasswordValidationError.PASSWORD_MISMATCH

/**
 * Returns password error text
 */
actual fun ResourceWrapper.getPasswordError(error: PasswordValidationError): String = when(error) {
    PASSWORD_LENGTH -> "Password should be at least 6 chars long"
    PASSWORD_MISMATCH -> "Passwords mismatch"
}
