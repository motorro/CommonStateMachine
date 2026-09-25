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

import com.motorro.commonstatemachine.examples.welcome.login.data.LoginDataState
import com.motorro.commonstatemachine.examples.welcome.login.data.LoginGesture
import com.motorro.commonstatemachine.examples.welcome.login.usecase.CheckCredentials
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

/**
 * Emulates login operation
 */
internal class CredentialsCheckState(
    context: LoginContext,
    private val data: LoginDataState,
    private val checkCredentials: CheckCredentials
) : LoginState(context) {

    /**
     * Should have valid password at this point
     */
    private val password = requireNotNull(data.password) {
        "Password is not provided"
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(renderer.renderLoading(data))
        Napier.d("Checking for user credentials...")
        stateScope.launch {
            val valid = checkCredentials(data.email, password)
            if (valid) {
                Napier.d("Correct credentials. Transferring to complete screen...")
                host.complete()
            } else {
                Napier.w("Login error. Transferring to error screen...")
                setMachineState(factory.error(data, IllegalArgumentException("Wrong username or password")))
            }
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) = when(gesture) {
        LoginGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onBack() {
        Napier.d("Returning to password entry")
        setMachineState(factory.passwordEntry(data))
    }

    @Factory
    class StateFactory(private val checkCredentials: CheckCredentials) {
        operator fun invoke(
            context: LoginContext,
            data: LoginDataState
        ): LoginState = CredentialsCheckState(
            context,
            data,
            checkCredentials
        )
    }
}