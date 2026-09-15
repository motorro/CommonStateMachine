package com.motorro.statemachine.skills.auth.implementation.data

import com.motorro.statemachine.skills.auth.api.AuthGesture

/**
 * All gestures available in this feature
 */
internal sealed class AuthGestureImpl : AuthGesture {
    /**
     * Backwards navigation element clicked
     */
    data object Back : AuthGestureImpl()

    /**
     * Default call-to-action clicked
     */
    data object Action : AuthGestureImpl()

    /**
     * Gestures for the login form
     */
    sealed class Form : AuthGestureImpl() {
        /**
         * Username input changed
         */
        data class UsernameChanged(val value: String) : Form()

        /**
         * Password input changed
         */
        data class PasswordChanged(val value: String) : Form()
    }
}