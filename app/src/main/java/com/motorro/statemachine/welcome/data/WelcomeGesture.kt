package com.motorro.statemachine.welcome.data

import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.register.data.RegisterGesture

/**
 * Welcome flow UI gestures
 */
sealed class WelcomeGesture {
    /**
     * Go back
     */
    object Back : WelcomeGesture()

    /**
     * Go forward (default call to action)
     */
    object Action: WelcomeGesture()

    /**
     * Terms and conditions switch toggled
     */
    object TermsAndConditionsToggled : WelcomeGesture()

    /**
     * Email field changed
     * @property value Email value
     */
    data class EmailChanged(val value: String) : WelcomeGesture()

    /**
     * Login flow gesture
     * @property value Login flow gesture
     */
    data class Login(val value: LoginGesture) : WelcomeGesture()

    /**
     * Register flow gesture
     * @property value Register flow gesture
     */
    data class Register(val value: RegisterGesture) : WelcomeGesture()
}