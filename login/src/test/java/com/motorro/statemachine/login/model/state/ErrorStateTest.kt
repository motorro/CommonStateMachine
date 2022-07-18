package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.commonapi.welcome.data.BAD
import com.motorro.statemachine.commonapi.welcome.data.WelcomeDataState
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class ErrorStateTest : BaseStateTest() {
    private val password = "password"
    private val data = LoginDataState(WelcomeDataState(BAD), password)
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