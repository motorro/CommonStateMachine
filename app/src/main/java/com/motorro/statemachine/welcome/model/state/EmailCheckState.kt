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

import com.motorro.statemachine.commonapi.welcome.data.WelcomeDataState
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.usecase.CheckEmail
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Switches login/registration flow based on hardcoded addresses
 */
class EmailCheckState(
    context: WelcomeContext,
    private val data: WelcomeDataState,
    private val checkEmail: CheckEmail
) : WelcomeState(context) {

    /**
     * Should have valid email at this point
     */
    private val email = requireNotNull(data.email) {
        "Email is not provided"
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(renderer.renderChecking(data))
        Timber.d("Checking if user exists...")
        stateScope.launch {
            val exists = checkEmail(email)
            if (exists) {
                Timber.d("Existing user. Transferring to login flow...")
                setMachineState(factory.loginFlow(data))
            } else {
                Timber.d("New user. Transferring to registration flow...")
                setMachineState(factory.registrationFlow(data))
            }
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: WelcomeGesture) = when (gesture) {
        is WelcomeGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    /**
     * Exits app
     */
    private fun onBack() {
        Timber.d("Going back to email entry...")
        setMachineState(factory.emailEntry(data))
    }

    @ViewModelScoped
    class Factory @Inject constructor(private val checkEmail: CheckEmail) {
        operator fun invoke(
            context: WelcomeContext,
            data: WelcomeDataState
        ): WelcomeState = EmailCheckState(
            context,
            data,
            checkEmail
        )
    }
}