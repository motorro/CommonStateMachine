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
import com.motorro.statemachine.di.api.data.Session
import com.motorro.statemachine.di.login.LoginConstants
import com.motorro.statemachine.di.login.data.LoginDataState
import com.motorro.statemachine.di.login.data.LoginGesture
import com.motorro.statemachine.di.login.data.LoginUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

/**
 * Emulates login
 */
internal class LoggingIn(context: LoginContext, private val data: LoginDataState) : BaseLoginState(context) {
    override fun doStart() {
        setUiState(LoginUiState.Loading)
        login()
    }

    private fun login() = stateScope.launch {
        Logger.d("Logging in...")
        delay(2.seconds)
        if (data.username == LoginConstants.USERNAME && data.password == LoginConstants.PASSWORD) {
            Logger.d("Login successful")
            setMachineState(factory.complete(Session.Active(
                accessToken = LoginConstants.ACCESS_TOKEN,
                refreshToken = LoginConstants.REFRESH_TOKEN
            )))
        } else {
            Logger.d("Login failed")
            setMachineState(factory.error(
                data,
                IllegalArgumentException("Invalid username or password")
            ))
        }
    }

    override fun doProcess(gesture: LoginGesture) {
        when(gesture) {
            LoginGesture.Back -> {
                Logger.d("Back pressed. Back to form...")
                setMachineState(factory.form(data))
            }
            else -> super.doProcess(gesture)
        }
    }
}