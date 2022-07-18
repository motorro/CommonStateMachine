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
import com.motorro.statemachine.register.data.PasswordValidationError
import com.motorro.statemachine.register.data.PasswordValidationError.PASSWORD_LENGTH
import com.motorro.statemachine.register.data.PasswordValidationError.PASSWORD_MISMATCH
import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.data.RegisterGesture

/**
 * Password entry screen
 */
class PasswordEntryState(
    context: RegisterContext,
    private var data: RegisterDataState
) : RegisterState(context) {

    /**
     * Should have valid email at this point
     */
    private val email = requireNotNull(data.commonData.email) {
        "Email is not provided"
    }

    /**
     * Local state - repeat password
     */
    private var repeatPassword: String? = null

    /**
     * Validation is turned on only if user has submitted invalid data
     */
    private var validate: Boolean = false

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        render()
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: RegisterGesture) = when(gesture) {
        RegisterGesture.Action -> onAction()
        RegisterGesture.Back -> onBack()
        is RegisterGesture.PasswordChanged -> onPasswordChanged(gesture)
        is RegisterGesture.RepeatPasswordChanged -> onRepeatPasswordChanged(gesture)
    }

    private fun onAction() {
        if (arePasswordsValid()) {
            Logger.d("Valid passwords. Transferring to credentials check")
            setMachineState(factory.registering(data))
        } else {
            validate = true
            render()
        }
    }

    private fun onBack() {
        Logger.d("Returning to e-mail entry...")
        host.backToEmailEntry(data.commonData)
    }

    private fun onPasswordChanged(gesture: RegisterGesture.PasswordChanged) {
        data = data.copy(password = gesture.value)
        render()
    }

    private fun onRepeatPasswordChanged(gesture: RegisterGesture.RepeatPasswordChanged) {
        repeatPassword = gesture.value
        render()
    }

    private fun render() {
        setUiState(
            renderer.renderPasswordEntry(
                data,
                repeatPassword,
                getValidationError().takeIf { validate }
            )
        )
    }

    private fun arePasswordsValid(): Boolean = null == getValidationError()

    private fun isPasswordValid(value: String?): Boolean = null != value?.takeIf { it.length >= 6 }

    private fun getValidationError(): PasswordValidationError? = when {
        isPasswordValid(data.password).not() -> PASSWORD_LENGTH
        isPasswordValid(repeatPassword).not() -> PASSWORD_LENGTH
        data.password != repeatPassword -> PASSWORD_MISMATCH
        else -> null
    }
}

