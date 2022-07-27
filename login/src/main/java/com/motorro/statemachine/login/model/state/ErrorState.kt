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

import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import timber.log.Timber

/**
 * Login error
 */
internal class ErrorState(
    context: LoginContext,
    private val data: LoginDataState,
    private val error: Throwable
) : LoginState(context) {

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) = when(gesture) {
        LoginGesture.Back, LoginGesture.Action -> onDismiss()
        else -> super.doProcess(gesture)
    }

    private fun onDismiss() {
        Timber.d("Returning to password entry")
        setMachineState(factory.passwordEntry(data))
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(renderer.renderError(data, error))
    }
}