package com.motorro.statemachine.register.data

/**
 * Login flow UI gestures
 */
sealed class RegisterGesture {
    /**
     * Go back
     */
    object Back : RegisterGesture()

    /**
     * Go forward (default call to action)
     */
    object Action: RegisterGesture()

    /**
     * Password field changed
     * @property value Password value
     */
    data class PasswordChanged(val value: String) : RegisterGesture()

    /**
     * Repeat password field changed
     * @property value Password value
     */
    data class RepeatPasswordChanged(val value: String) : RegisterGesture()
}