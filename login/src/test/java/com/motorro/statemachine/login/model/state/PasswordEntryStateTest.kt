package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.welcome.data.GOOD
import com.motorro.statemachine.welcome.data.WelcomeDataState
import io.mockk.*
import org.junit.Test

internal class PasswordEntryStateTest : BaseStateTest() {

    private fun createState(
        data: LoginDataState = LoginDataState(WelcomeDataState(GOOD))
    ) = PasswordEntryState(
        context,
        data
    )

    @Test
    fun displaysNonValidStateOnEmptyPassword() {
        val data = LoginDataState(WelcomeDataState(GOOD))
        val state = createState()

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(R_PASSWORD)
        }
        verify {
            renderer.renderPassword(data, false)
        }
    }

    @Test
    fun displaysValidStateOnValidPassword() {
        val password = "password"
        val data = LoginDataState(WelcomeDataState(GOOD), password)
        val state = createState(data)

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(R_PASSWORD)
        }
        verify {
            renderer.renderPassword(data, true)
        }
    }

    @Test
    fun updatesPassword() {
        val password = "password"
        val data = LoginDataState(WelcomeDataState(GOOD), "")
        val state = createState(data)

        state.start(stateMachine)
        state.process(LoginGesture.PasswordChanged(password))

        verify(exactly = 2) {
            stateMachine.setUiState(R_PASSWORD)
        }

        verifyOrder {
            renderer.renderPassword(data, false)
            renderer.renderPassword(data.copy(password = password), true)
        }
    }

    @Test
    fun transfersToCheckIfValid() {
        val password = "password"
        val data = LoginDataState(WelcomeDataState(GOOD), password)
        val state = createState(data)
        val checking: LoginState = mockk()
        every { factory.checking(any()) } returns checking

        state.start(stateMachine)
        state.process(LoginGesture.Action)

        verify { stateMachine.setMachineState(checking) }
        verify { factory.checking(data) }
    }

    @Test
    fun doesNotTransferToCheckIfNotValid() {
        val password = ""
        val data = LoginDataState(WelcomeDataState(GOOD), password)
        val state = createState(data)

        state.start(stateMachine)
        state.process(LoginGesture.Action)

        verify(exactly = 0) { stateMachine.setMachineState(any()) }
        verify(exactly = 0) { factory.checking(data) }
    }

    @Test
    fun returnsToEmailEntryOnBack() {
        val data = LoginDataState(WelcomeDataState(GOOD))
        every { host.backToEmailEntry(any()) } just Runs
        val state = createState()

        state.start(stateMachine)
        state.process(LoginGesture.Back)

        verify { host.backToEmailEntry(data.commonData) }
    }
}