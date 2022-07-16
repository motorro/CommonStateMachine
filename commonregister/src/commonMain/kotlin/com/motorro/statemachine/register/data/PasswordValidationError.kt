package com.motorro.statemachine.register.data

enum class PasswordValidationError(val resId: Int) {
    PASSWORD_LENGTH(10),
    PASSWORD_MISMATCH(20)
}