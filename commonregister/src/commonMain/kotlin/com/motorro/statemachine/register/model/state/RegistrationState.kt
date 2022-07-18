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

package com.motorro.statemachine.register.model.state

import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.data.RegisterGesture
import com.motorro.statemachine.register.usecase.Registration
import kotlinx.coroutines.launch

/**
 * Emulates login operation
 */
class RegistrationState(
    context: RegisterContext,
    private val data: RegisterDataState,
    private val register: Registration
) : RegisterState(context) {

    /**
     * Should have valid email at this point
     */
    private val email = requireNotNull(data.commonData.email) {
        "Email is not provided"
    }

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
        setUiState(renderer.renderRegistration(data))
        Logger.d("Registering user...")
        stateScope.launch {
            register(email, password)
            Logger.d("Registered. Transferring to complete screen...")
            host.complete(email)
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: RegisterGesture) = when(gesture) {
        RegisterGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onBack() {
        Logger.d("Returning to password entry")
        setMachineState(factory.passwordEntry(data))
    }

    /**
     * [RegisterState] factory
     */
    class Factory(private val register: Registration) {
        operator fun invoke(
            context: RegisterContext,
            data: RegisterDataState
        ): RegisterState = RegistrationState(
            context,
            data,
            register
        )
    }
}