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

import com.motorro.statemachine.commonapi.welcome.data.GOOD
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import io.mockk.*
import org.junit.Test

internal class PasswordEntryStateTest : BaseStateTest() {

    private fun createState(
        data: LoginDataState = LoginDataState(GOOD)
    ) = PasswordEntryState(
        context,
        data
    )

    @Test
    fun displaysNonValidStateOnEmptyPassword() {
        val data = LoginDataState(GOOD)
        val state = createState()

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(R_PASSWORD)
        }
        verify {
            renderer.renderPassword(data, false)
        }
    }

    @Test
    fun displaysValidStateOnValidPassword() {
        val password = "password"
        val data = LoginDataState(GOOD, password)
        val state = createState(data)

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(R_PASSWORD)
        }
        verify {
            renderer.renderPassword(data, true)
        }
    }

    @Test
    fun updatesPassword() {
        val password = "password"
        val data = LoginDataState(GOOD, "")
        val state = createState(data)

        state.start(stateMachine)
        state.process(LoginGesture.PasswordChanged(password))

        verify(exactly = 2) {
            stateMachine.setUiState(R_PASSWORD)
        }

        verifyOrder {
            renderer.renderPassword(data, false)
            renderer.renderPassword(data.copy(password = password), true)
        }
    }

    @Test
    fun transfersToCheckIfValid() {
        val password = "password"
        val data = LoginDataState(GOOD, password)
        val state = createState(data)
        val checking: LoginState = mockk()
        every { factory.checking(any()) } returns checking

        state.start(stateMachine)
        state.process(LoginGesture.Action)

        verify { stateMachine.setMachineState(checking) }
        verify { factory.checking(data) }
    }

    @Test
    fun doesNotTransferToCheckIfNotValid() {
        val password = ""
        val data = LoginDataState(GOOD, password)
        val state = createState(data)

        state.start(stateMachine)
        state.process(LoginGesture.Action)

        verify(exactly = 0) { stateMachine.setMachineState(any()) }
        verify(exactly = 0) { factory.checking(data) }
    }

    @Test
    fun returnsToEmailEntryOnBack() {
        every { host.backToEmailEntry() } just Runs
        val state = createState()

        state.start(stateMachine)
        state.process(LoginGesture.Back)

        verify { host.backToEmailEntry() }
    }
}