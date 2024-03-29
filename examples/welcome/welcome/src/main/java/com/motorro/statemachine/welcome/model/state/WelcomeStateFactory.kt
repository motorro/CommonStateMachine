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

package com.motorro.statemachine.welcome.model.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.welcome.data.WelcomeDataState
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import com.motorro.statemachine.welcome.model.WelcomeRenderer
import dagger.hilt.android.scopes.ViewModelScoped
import timber.log.Timber
import javax.inject.Inject

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

    /**
     * [WelcomeStateFactory] implementation
     */
    @ViewModelScoped
    class Impl @Inject constructor(
        savedStateHandle: SavedStateHandle,
        renderer: WelcomeRenderer,
        private val createPreloading: PreloadingState.Factory,
        private val createLogin: LoginFlowState.Factory,
        private val createRegister: RegistrationFlowState.Factory,
        private val createEmailCheck: EmailCheckState.Factory
    ) : WelcomeStateFactory {

        private val context: WelcomeContext = object : WelcomeContext {
            override val factory = this@Impl
            override val savedStateHandle = savedStateHandle
            override val renderer: WelcomeRenderer = renderer
        }

        /**
         * Preloads 'data' for registration
         */
        override fun preload(): CommonMachineState<WelcomeGesture, WelcomeUiState> {
            Timber.d("Creating 'Preloading'...")
            return createPreloading(context)
        }

        /**
         * Creates welcome screen state
         * @param welcomeGreeting Message to display on welcome screen
         */
        override fun welcome(welcomeGreeting: String): CommonMachineState<WelcomeGesture, WelcomeUiState> {
            Timber.d("Creating 'Welcome'...")
            return TermsAndConditionsState(context, welcomeGreeting)
        }

        /**
         * Creates email-entry state
         * @param data Data state
         */
        override fun emailEntry(data: WelcomeDataState?): CommonMachineState<WelcomeGesture, WelcomeUiState> {
            Timber.d("Creating 'Email entry'...")
            return EmailEntryState(context, data)
        }

        /**
         * Checks if email is registered
         * @param data Data state
         */
        override fun checkEmail(data: WelcomeDataState): CommonMachineState<WelcomeGesture, WelcomeUiState> {
            Timber.d("Creating 'Check e-mail'...")
            return createEmailCheck(context, data)
        }

        /**
         * Enter existing user password
         * @param data Data state
         */
        override fun loginFlow(data: WelcomeDataState): CommonMachineState<WelcomeGesture, WelcomeUiState> {
            Timber.d("Creating 'Login flow'...")
            return createLogin(context, data)
        }

        /**
         * Enter registration user password
         * @param data Data state
         */
        override fun registrationFlow(data: WelcomeDataState): CommonMachineState<WelcomeGesture, WelcomeUiState> {
            Timber.d("Creating 'Registration flow'...")
            return createRegister(context, data)
        }

        /**
         * Registration complete state
         * @param email Registered user's email
         */
        override fun complete(email: String): CommonMachineState<WelcomeGesture, WelcomeUiState> {
            Timber.d("Creating 'Complete'...")
            return CompleteState(context, email)
        }

        /**
         * Terminates registration flow
         */
        override fun terminate(): CommonMachineState<WelcomeGesture, WelcomeUiState> {
            Timber.d("Creating 'Terminated'...")
            return TerminatedState(context)
        }

    }
}