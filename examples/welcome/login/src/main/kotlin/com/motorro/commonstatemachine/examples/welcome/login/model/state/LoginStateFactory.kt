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

package com.motorro.commonstatemachine.examples.welcome.login.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.examples.welcome.login.LoginFlowStarter
import com.motorro.commonstatemachine.examples.welcome.login.data.LoginDataState
import com.motorro.commonstatemachine.examples.welcome.login.data.LoginGesture
import com.motorro.commonstatemachine.examples.welcome.login.data.LoginUiState

/**
 * Login flow state factory
 */
internal interface LoginStateFactory : LoginFlowStarter {
    /**
     * Creates a starting state
     * @param email Common data state
     */
    override fun start(email: String): CommonMachineState<LoginGesture, LoginUiState> = passwordEntry(LoginDataState(email))

    /**
     * Enter existing user password
     * @param data Login data state
     */
    fun passwordEntry(data: LoginDataState): LoginState

    /**
     * Checks email/password
     * @param data Data state
     */
    abstract fun checking(data: LoginDataState): LoginState

    /**
     * Password error screen
     */
    abstract fun error(data: LoginDataState, error: Throwable): LoginState

}

