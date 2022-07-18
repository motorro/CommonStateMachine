package com.motorro.statemachine.welcome.data

import com.motorro.statemachine.login.data.LoginGesture

/**
 * Welcome flow UI gestures
 */
sealed class WelcomeGesture {
    /**
     * Go back
     */
    object Back : com.motorro.statemachine.welcome.data.WelcomeGesture()

    /**
     * Go forward (default call to action)
     */
    object Action: com.motorro.statemachine.welcome.data.WelcomeGesture()

    /**
     * Terms and conditions switch toggled
     */
    object TermsAndConditionsToggled : com.motorro.statemachine.welcome.data.WelcomeGesture()

    /**
     * Email field changed
     * @property value Email value
     */
    data class EmailChanged(val value: String) : com.motorro.statemachine.welcome.data.WelcomeGesture()

    /**
     * Login flow gesture
     * @property value Login flow gesture
     */
    data class Login(val value: LoginGesture) : com.motorro.statemachine.welcome.data.WelcomeGesture()
}