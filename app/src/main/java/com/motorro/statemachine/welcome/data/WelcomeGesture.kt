/*
 * Copyright 2022 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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