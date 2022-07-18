package com.motorro.statemachine.register.model

import com.motorro.statemachine.commoncore.resources.ResourceWrapper
import com.motorro.statemachine.commonregister.R
import com.motorro.statemachine.register.data.PasswordValidationError
import com.motorro.statemachine.register.data.PasswordValidationError.*

/**
 * Returns password error text
 */
actual fun ResourceWrapper.getPasswordError(error: PasswordValidationError): String = when(error) {
    PASSWORD_LENGTH -> getString(R.string.register_error_password_length)
    PASSWORD_MISMATCH -> getString(R.string.register_error_password_mismatch)
}
