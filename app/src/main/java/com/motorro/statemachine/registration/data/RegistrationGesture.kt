package com.motorro.statemachine.registration.data

/**
 * Registration flow UI gestures
 */
sealed class RegistrationGesture {
    /**
     * Go back
     */
    object Back : RegistrationGesture()

    /**
     * Go forward (default call to action)
     */
    object Action: RegistrationGesture()

    /**
     * Terms and conditions switch toggled
     */
    object TermsAndConditionsToggled : RegistrationGesture()

    /**
     * Email field changed
     * @property value Email value
     */
    data class EmailChanged(val value: String) : RegistrationGesture()

    /**
     * Password field changed
     * @property value Password value
     */
    data class PasswordChanged(val value: String) : RegistrationGesture()

    /**
     * Password repeat field changed
     * @property value Password value
     */
    data class RepeatPasswordChanged(val value: String) : RegistrationGesture()
}