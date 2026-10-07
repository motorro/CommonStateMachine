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

package com.motorro.commonstatemachine.examples.welcome.app.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.examples.welcome.app.data.WelcomeDataState
import com.motorro.commonstatemachine.examples.welcome.app.data.WelcomeGesture
import com.motorro.commonstatemachine.examples.welcome.app.data.WelcomeUiState

/**
 * Welcome state factory
 */
interface WelcomeStateFactory {
    /**
     * Preloads 'data' for registration
     */
    fun preload(): CommonMachineState<WelcomeGesture, WelcomeUiState>

    /**
     * Creates welcome screen state
     * @param welcomeGreeting Message to display on welcome screen
     */
    fun welcome(welcomeGreeting: String): CommonMachineState<WelcomeGesture, WelcomeUiState>

    /**
     * Creates email-entry state
     * @param data Data state
     */
    fun emailEntry(data: WelcomeDataState? = null): CommonMachineState<WelcomeGesture, WelcomeUiState>

    /**
     * Checks if email is registered
     * @param data Data state
     */
    fun checkEmail(data: WelcomeDataState): CommonMachineState<WelcomeGesture, WelcomeUiState>

    /**
     * Enter existing user password
     * @param data Data state
     */
    fun loginFlow(data: WelcomeDataState): CommonMachineState<WelcomeGesture, WelcomeUiState>

    /**
     * Enter registration user password
     * @param data Data state
     */
    fun registrationFlow(data: WelcomeDataState): CommonMachineState<WelcomeGesture, WelcomeUiState>

    /**
     * Registration complete state
     * @param email Registered user's email
     */
    fun complete(email: String) : CommonMachineState<WelcomeGesture, WelcomeUiState>

    /**
     * Terminates registration flow
     */
    fun terminate() : CommonMachineState<WelcomeGesture, WelcomeUiState>

}

