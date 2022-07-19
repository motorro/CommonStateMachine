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

import androidx.lifecycle.SavedStateHandle
import com.motorro.statemachine.commonapi.welcome.data.WelcomeDataState
import com.motorro.statemachine.welcome.data.WelcomeGesture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

internal class EmailEntryStateTest : BaseStateTest() {

    private fun createState(data: WelcomeDataState? = WelcomeDataState()) = EmailEntryState(
        context,
        data
    )

    @Test
    fun displaysNonValidStateOnEmptyEmail() {
        val state = createState()

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(R_CONTENT)
        }
        verify {
            renderer.renderEmailEntry(WelcomeDataState(), false)
        }
    }

    @Test
    fun displaysValidStateOnValidEmail() {
        val email = "test@example.com"
        val data = WelcomeDataState(email)
        val state = createState(data)

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(R_CONTENT)
        }
        verify {
            renderer.renderEmailEntry(data, true)
        }
    }

    @Test
    fun restoresEmailFromSavedState() {
        val email = "test@example.com"
        val saved = SavedStateHandle(mapOf(EmailEntryState.EMAIL_KEY to email))
        every { context.savedStateHandle } returns saved
        val state = createState(null)

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(R_CONTENT)
        }
        verify {
            renderer.renderEmailEntry(WelcomeDataState(email), true)
        }
    }

    @Test
    fun updatesEmail() {
        val email = "test@example.com"
        val state = createState()

        state.start(stateMachine)
        state.process(WelcomeGesture.EmailChanged(email))

        verify(exactly = 2) {
            stateMachine.setUiState(R_CONTENT)
        }
        verifyOrder {
            renderer.renderEmailEntry(WelcomeDataState(), false)
            renderer.renderEmailEntry(WelcomeDataState(email), true)
        }
    }

    @Test
    fun transfersToCheckIfValid() {
        val email = "test@example.com"
        val data = WelcomeDataState(email = email)
        val state = createState(data)
        val checking: WelcomeState = mockk()
        every { factory.checkEmail(any()) } returns checking

        state.start(stateMachine)
        state.process(WelcomeGesture.Action)

        verify { stateMachine.setMachineState(checking) }
        verify { factory.checkEmail(data) }
    }

    @Test
    fun doesNotTransferToCheckIfNotValid() {
        val email = ""
        val data = WelcomeDataState(email = email)
        val state = createState(data)

        state.start(stateMachine)
        state.process(WelcomeGesture.Action)

        verify(exactly = 0) { stateMachine.setMachineState(any()) }
        verify(exactly = 0) { factory.checkEmail(data) }
    }

    @Test
    fun terminatesOnBack() {
        val terminated: WelcomeState = mockk()
        every { factory.terminate() } returns terminated
        val state = createState()

        state.start(stateMachine)
        state.process(WelcomeGesture.Back)

        verify { stateMachine.setMachineState(terminated) }
        verify { factory.terminate() }
    }
}