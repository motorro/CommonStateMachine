package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

class WelcomeStateTest : BaseStateTest() {
    private val greeting = "Welcome"
    private val state = WelcomeState(context, greeting)

    @Test
    fun displaysWelcomeScreenOnStart() {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                RegistrationUiState.Welcome(
                    message = greeting,
                    termsAccepted = false,
                    actionEnabled = false
                )
            )
        }
    }

    @Test
    fun togglesTermsSwitchEnablingNext() {
        state.start(stateMachine)
        state.process(RegistrationGesture.TermsAndConditionsToggled)

        verifyOrder {
            stateMachine.setUiState(
                RegistrationUiState.Welcome(
                    message = greeting,
                    termsAccepted = false,
                    actionEnabled = false
                )
            )
            stateMachine.setUiState(
                RegistrationUiState.Welcome(
                    message = greeting,
                    termsAccepted = true,
                    actionEnabled = true
                )
            )
        }
    }

    @Test
    fun transfersToEmailEntryWhenTermsAccepted() {
        val email: RegistrationState = mockk()
        every { factory.emailEntry(any()) } returns email

        state.start(stateMachine)
        state.process(RegistrationGesture.TermsAndConditionsToggled)
        state.process(RegistrationGesture.Action)

        verify { stateMachine.setMachineState(email) }
        verify { factory.emailEntry() }
    }

    @Test
    fun willNotTransferToEmailEntryWhenTermsNotAccepted() {
        state.start(stateMachine)
        state.process(RegistrationGesture.Action)

        verify(exactly = 0) { stateMachine.setMachineState(any()) }
        verify(exactly = 0) { factory.emailEntry() }
    }

    @Test
    fun terminatesOnBack() {
        val terminated: RegistrationState = mockk()
        every { factory.terminate() } returns terminated

        state.start(stateMachine)
        state.process(RegistrationGesture.Back)

        verify { stateMachine.setMachineState(terminated) }
        verify { factory.terminate() }
    }
}