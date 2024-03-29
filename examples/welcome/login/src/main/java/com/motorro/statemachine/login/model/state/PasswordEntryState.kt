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
 * Password entry screen
 */
internal class PasswordEntryState(
    context: LoginContext,
    private var data: LoginDataState
) : LoginState(context) {

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        render()
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) = when(gesture) {
        LoginGesture.Action -> onAction()
        LoginGesture.Back -> onBack()
        is LoginGesture.PasswordChanged -> onPasswordChanged(gesture)
    }

    private fun onAction() {
        if (isPasswordValid()) {
            Timber.d("Valid password. Transferring to credentials check")
            setMachineState(factory.checking(data))
        }
    }

    private fun onBack() {
        Timber.d("Returning to e-mail entry...")
        host.backToEmailEntry()
    }

    private fun onPasswordChanged(gesture: LoginGesture.PasswordChanged) {
        data = data.copy(password = gesture.value)
        render()
    }

    private fun render() {
        setUiState(renderer.renderPassword(data, isPasswordValid()))
    }

    private fun isPasswordValid(): Boolean = null != data.password?.takeIf { it.length >= 6 }
}

