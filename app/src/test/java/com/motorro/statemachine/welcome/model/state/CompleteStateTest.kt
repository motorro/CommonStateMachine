package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.welcome.data.GOOD
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
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

        verify { stateMachine.setUiState(WelcomeUiState.Complete(email)) }
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

    @Test
    fun terminatesOnAction() {
        val terminated: WelcomeState = mockk()
        every { factory.terminate() } returns terminated

        state.start(stateMachine)
        state.process(WelcomeGesture.Action)

        verify { stateMachine.setMachineState(terminated) }
        verify { factory.terminate() }
    }
}