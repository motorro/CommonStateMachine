package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import io.mockk.*
import org.junit.Test

class WelcomeStateTest : BaseStateTest() {
    private val state = WelcomeState(context, resourceWrapper)

    @Test
    fun displaysWelcomeScreenOnStart() {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                RegistrationUiState.Welcome(
                    message = R_STRING,
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
                    message = R_STRING,
                    termsAccepted = false,
                    actionEnabled = false
                )
            )
            stateMachine.setUiState(
                RegistrationUiState.Welcome(
                    message = R_STRING,
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

        verify { stateMachine.machineState = email }
        verify { factory.emailEntry() }
    }

    @Test
    fun willNotTransferToEmailEntryWhenTermsNotAccepted() {
        state.start(stateMachine)
        state.process(RegistrationGesture.Action)

        verify { stateMachine.machineState wasNot Called }
        verify { factory.emailEntry() wasNot Called }
    }

    @Test
    fun terminatesOnBack() {
        val terminated: RegistrationState = mockk()
        every { factory.terminate() } returns terminated

        state.start(stateMachine)
        state.process(RegistrationGesture.Back)

        verify { stateMachine.machineState = terminated }
        verify { factory.terminate() }
    }
}