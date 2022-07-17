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