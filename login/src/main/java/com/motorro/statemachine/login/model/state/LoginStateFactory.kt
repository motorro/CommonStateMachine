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

package com.motorro.statemachine.login.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.commonapi.welcome.data.WelcomeDataState
import com.motorro.statemachine.commonapi.welcome.model.state.FlowStarter
import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.login.di.LoginScope
import com.motorro.statemachine.login.model.LoginRenderer
import timber.log.Timber
import javax.inject.Inject

/**
 * Login flow state factory
 */
internal interface LoginStateFactory : FlowStarter<LoginGesture, LoginUiState> {
    /**
     * Creates a starting state
     * @param data Common data state
     */
    override fun start(data: WelcomeDataState): CommonMachineState<LoginGesture, LoginUiState> = passwordEntry(LoginDataState(data))

    /**
     * Enter existing user password
     * @param data Login data state
     */
    fun passwordEntry(data: LoginDataState): LoginState

    /**
     * Checks email/password
     * @param data Data state
     */
    fun checking(data: LoginDataState): LoginState

    /**
     * Password error screen
     */
    fun error(data: LoginDataState, error: Throwable): LoginState

    /**
     * [LoginStateFactory] implementation
     */
    @LoginScope
    class Impl @Inject constructor(
        host: WelcomeFeatureHost,
        renderer: LoginRenderer,
        private val createCredentialsCheck: CredentialsCheckState.Factory
    ) : LoginStateFactory {

        private val context: LoginContext = object : LoginContext {
            override val factory: LoginStateFactory = this@Impl
            override val host: WelcomeFeatureHost = host
            override val renderer: LoginRenderer = renderer
        }

        /**
         * Enter existing user password
         * @param data Common data state
         */
        override fun passwordEntry(data: LoginDataState): LoginState {
            Timber.d("Creating 'Password entry'...")
            return PasswordEntryState(context, data)
        }

        /**
         * Checks email/password
         * @param data Data state
         */
        override fun checking(data: LoginDataState): LoginState {
            Timber.d("Creating 'Credentials check'...")
            return createCredentialsCheck(context, data)
        }

        /**
         * Password error screen
         */
        override fun error(data: LoginDataState, error: Throwable): LoginState {
            Timber.d("Creating 'Error'...")
            return ErrorState(context, data, error)
        }
    }
}