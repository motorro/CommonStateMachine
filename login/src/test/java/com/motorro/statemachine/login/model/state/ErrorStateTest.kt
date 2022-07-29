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

import com.motorro.statemachine.commonapi.welcome.data.BAD
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class ErrorStateTest : BaseStateTest() {
    private val password = "password"
    private val data = LoginDataState(BAD, password)
    private val error = IllegalArgumentException("Incorrect email or password")
    private val passwordEntry: LoginState = mockk()

    init {
        every { factory.passwordEntry(any()) } returns passwordEntry
    }

    private val state = ErrorState(
        context,
        data,
        error
    )

    @Test
    fun displaysErrorOnStart() {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(R_ERROR)
        }
        verify {
            renderer.renderError(data, error)
        }
    }

    @Test
    fun returnsToPasswordEntryOnBack() {
        state.start(stateMachine)
        state.process(LoginGesture.Back)

        verify { stateMachine.setMachineState(passwordEntry) }
        verify { factory.passwordEntry(data) }
    }

    @Test
    fun returnsToPasswordEntryOnAction() {
        state.start(stateMachine)
        state.process(LoginGesture.Action)

        verify { stateMachine.setMachineState(passwordEntry) }
        verify { factory.passwordEntry(data) }
    }
}