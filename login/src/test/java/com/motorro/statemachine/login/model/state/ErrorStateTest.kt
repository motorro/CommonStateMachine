package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.registrationapi.data.BAD
import com.motorro.statemachine.registrationapi.data.RegistrationDataState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class ErrorStateTest : BaseStateTest() {
    private val password = "password"
    private val data = LoginDataState(RegistrationDataState(BAD), password)
    private val passwordEntry: LoginState = mockk()

    init {
        every { factory.passwordEntry(any()) } returns passwordEntry
    }

    private val state = ErrorState(
        context,
        data,
        IllegalArgumentException("Incorrect email or password"),
        resourceWrapper
    )

    @Test
    fun displaysErrorOnStart() {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                LoginUiState.LoginError(
                    LoginUiState.PasswordEntry(BAD, password, false),
                    R_STRING
                )
            )
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