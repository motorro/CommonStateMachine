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

import com.motorro.statemachine.welcome.data.WelcomeGesture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

class TermsAndConditionsStateTest : BaseStateTest() {
    private val greeting = "Welcome"
    private val state = TermsAndConditionsState(context, greeting)

    @Test
    fun displaysWelcomeScreenOnStart() {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                R_CONTENT
            )
        }
        verify {
            renderer.renderTerms(
                message = greeting,
                termsAccepted = false,
                nextEnabled = false
            )
        }
    }

    @Test
    fun togglesTermsSwitchEnablingNext() {
        state.start(stateMachine)
        state.process(WelcomeGesture.TermsAndConditionsToggled)

        verify(exactly = 2) {
            stateMachine.setUiState(
                R_CONTENT
            )
        }
        verifyOrder {
            renderer.renderTerms(
                message = greeting,
                termsAccepted = false,
                nextEnabled = false
            )
            renderer.renderTerms(
                message = greeting,
                termsAccepted = true,
                nextEnabled = true
            )
        }
    }

    @Test
    fun transfersToEmailEntryWhenTermsAccepted() {
        val email: WelcomeState = mockk()
        every { factory.emailEntry(any()) } returns email

        state.start(stateMachine)
        state.process(WelcomeGesture.TermsAndConditionsToggled)
        state.process(WelcomeGesture.Action)

        verify { stateMachine.setMachineState(email) }
        verify { factory.emailEntry() }
    }

    @Test
    fun willNotTransferToEmailEntryWhenTermsNotAccepted() {
        state.start(stateMachine)
        state.process(WelcomeGesture.Action)

        verify(exactly = 0) { stateMachine.setMachineState(any()) }
        verify(exactly = 0) { factory.emailEntry() }
    }

    @Test
    fun terminatesOnBack() {
        val terminated: WelcomeState = mockk()
        every { factory.terminate() } returns terminated

        state.start(stateMachine)
        state.process(WelcomeGesture.Back)

        verify { stateMachine.setMachineState(terminated) }
        verify { factory.terminate() }
    }
}