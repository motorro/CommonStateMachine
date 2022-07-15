package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.registrationapi.data.GOOD
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class CompleteStateTest : BaseStateTest() {
    private val email = GOOD
    private val state = CompleteState(context, email)

    @Test
    fun rendersEmailOnStart() {
        state.start(stateMachine)

        verify { stateMachine.setUiState(RegistrationUiState.Complete(email)) }
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

    @Test
    fun terminatesOnAction() {
        val terminated: RegistrationState = mockk()
        every { factory.terminate() } returns terminated

        state.start(stateMachine)
        state.process(RegistrationGesture.Action)

        verify { stateMachine.setMachineState(terminated) }
        verify { factory.terminate() }
    }
}