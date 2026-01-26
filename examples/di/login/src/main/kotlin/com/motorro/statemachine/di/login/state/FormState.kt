/*
 * Copyright 2026 Nikolai Kotchetkov.
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

package com.motorro.statemachine.di.login.state

import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.di.login.data.LoginDataState
import com.motorro.statemachine.di.login.data.LoginGesture
import com.motorro.statemachine.di.login.data.LoginUiState
import kotlin.properties.Delegates

internal class FormState(context: LoginContext, data: LoginDataState) : BaseLoginState(context) {

    private var data: LoginDataState by Delegates.observable(data) { _, _, newValue ->
        render(newValue)
    }

    override fun doStart() {
        render(data)
    }

    override fun doProcess(gesture: LoginGesture) {
        when (gesture) {
            LoginGesture.Action -> if (data.isLoginEnabled()) {
                Logger.d("Logging in...")
                setMachineState(factory.loggingIn(data))
            }
            LoginGesture.Back -> {
                Logger.d("Back pressed. Terminating...")
                setMachineState(factory.terminated())
            }
            is LoginGesture.PasswordChanged -> {
                data = data.copy(password = gesture.password)
            }
            is LoginGesture.UsernameChanged -> {
                data = data.copy(username = gesture.username)
            }
        }
    }

    private fun LoginDataState.isLoginEnabled(): Boolean = username.isNotBlank() && password.isNotBlank()

    private fun render(data: LoginDataState) {
        setUiState(LoginUiState.Form(
            username = data.username,
            password = data.password,
            loginEnabled = data.isLoginEnabled()
        ))
    }
}