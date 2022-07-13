package com.motorro.statemachine.login.data

/**
 * Login flow UI gestures
 */
sealed class LoginGesture {
    /**
     * Go back
     */
    object Back : LoginGesture()

    /**
     * Go forward (default call to action)
     */
    object Action: LoginGesture()

    /**
     * Password field changed
     * @property value Password value
     */
    data class PasswordChanged(val value: String) : LoginGesture()
}