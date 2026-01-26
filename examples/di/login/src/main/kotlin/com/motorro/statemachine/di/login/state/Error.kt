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

/**
 * Displays error
 */
internal class Error(
    context: LoginContext,
    private val data: LoginDataState,
    private val error: Throwable
) : BaseLoginState(context) {
    override fun doStart() {
        setUiState(LoginUiState.Error(error))
    }

    override fun doProcess(gesture: LoginGesture) {
        when(gesture) {
            LoginGesture.Back, LoginGesture.Action -> {
                Logger.d("Dismissed. Back to form...")
                setMachineState(factory.form(data))
            }
            else -> super.doProcess(gesture)
        }
    }
}