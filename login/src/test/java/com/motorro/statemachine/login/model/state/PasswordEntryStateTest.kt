package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.registrationapi.data.GOOD
import com.motorro.statemachine.registrationapi.data.RegistrationDataState
import io.mockk.*
import org.junit.Test

internal class PasswordEntryStateTest : BaseStateTest() {

    private fun createState(
        data: LoginDataState = LoginDataState(RegistrationDataState(GOOD))
    ) = PasswordEntryState(
        context,
        data
    )

    @Test
    fun displaysNonValidStateOnEmptyEmail() {
        val state = createState()

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                LoginUiState.PasswordEntry(GOOD, "", false)
            )
        }
    }

    @Test
    fun displaysValidStateOnValidPassword() {
        val password = "password"
        val state = createState(LoginDataState(RegistrationDataState(GOOD), password))

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                LoginUiState.PasswordEntry(GOOD, password, true)
            )
        }
    }

    @Test
    fun updatesPassword() {
        val password = "password"
        val state = createState(LoginDataState(RegistrationDataState(GOOD), ""))

        state.start(stateMachine)
        state.process(LoginGesture.PasswordChanged(password))

        verifyOrder {
            stateMachine.setUiState(
                LoginUiState.PasswordEntry(GOOD, "", false)
            )
            stateMachine.setUiState(
                LoginUiState.PasswordEntry(GOOD, password, true)
            )
        }
    }

    @Test
    fun transfersToCheckIfValid() {
        val password = "password"
        val data = LoginDataState(RegistrationDataState(GOOD), password)
        val state = createState(LoginDataState(RegistrationDataState(GOOD), password))
        val checking: LoginState = mockk()
        every { factory.checking(any()) } returns checking

        state.start(stateMachine)
        state.process(LoginGesture.Action)

        verify { stateMachine.machineState = checking }
        verify { factory.checking(data) }
    }

    @Test
    fun doesNotTransferToCheckIfNotValid() {
        val password = ""
        val data = LoginDataState(RegistrationDataState(GOOD), password)
        val state = createState(LoginDataState(RegistrationDataState(GOOD), password))

        state.start(stateMachine)
        state.process(LoginGesture.Action)

        verify(exactly = 0) { stateMachine.machineState = any() }
        verify(exactly = 0) { factory.checking(data) }
    }

    @Test
    fun returnsToEmailEntryOnBack() {
        val data = LoginDataState(RegistrationDataState(GOOD))
        every { host.backToEmailEntry(any()) } just Runs
        val state = createState()

        state.start(stateMachine)
        state.process(LoginGesture.Back)

        verify { host.backToEmailEntry(data.commonData) }
    }
}