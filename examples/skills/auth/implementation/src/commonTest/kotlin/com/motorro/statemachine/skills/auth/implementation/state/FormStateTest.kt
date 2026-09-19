package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.statemachine.skills.auth.api.AuthResult
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.EMPTY_STATE
import com.motorro.statemachine.skills.auth.implementation.data.PASSWORD_CHANGED
import com.motorro.statemachine.skills.auth.implementation.data.UI_STATE
import com.motorro.statemachine.skills.auth.implementation.data.USERNAME_CHANGED
import com.motorro.statemachine.skills.auth.implementation.data.VALID_FORM_STATE
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import kotlin.test.Test
import kotlin.test.assertEquals

internal class FormStateTest : BaseStateTest() {
    private lateinit var state: FormState

    override fun doInit() {
        state = FormState(context, EMPTY_STATE)
        every { renderer.renderForm(any(), any()) } returns UI_STATE
    }

    @Test
    fun rendersFormOnStart() = test {
        state.start(stateMachine)
        verify {
            renderer.renderForm(EMPTY_STATE, isValid = false)
            stateMachine.setUiState(UI_STATE)
        }
    }

    @Test
    fun processesUsernameChanged() = test {
        state.start(stateMachine)
        state.process(USERNAME_CHANGED)

        val expectedData = EMPTY_STATE.copy(username = USERNAME_CHANGED.value)
        verify(mode = VerifyMode.order) {
            renderer.renderForm(EMPTY_STATE, isValid = false)
            renderer.renderForm(expectedData, isValid = false)
        }
        assertEquals(USERNAME_CHANGED.value, state.data.username)
    }

    @Test
    fun processesPasswordChanged() = test {
        state.start(stateMachine)
        state.process(PASSWORD_CHANGED)

        val expectedData = EMPTY_STATE.copy(password = PASSWORD_CHANGED.value)
        verify(mode = VerifyMode.order) {
            renderer.renderForm(EMPTY_STATE, isValid = false)
            renderer.renderForm(expectedData, isValid = false)
        }
        assertEquals(PASSWORD_CHANGED.value, state.data.password)
    }

    @Test
    fun processesAction() = test {
        every { stateFactory.authenticating(any()) } returns nextState
        state.data = VALID_FORM_STATE

        state.start(stateMachine)
        state.process(AuthGestureImpl.Action)

        verify {
            renderer.renderForm(VALID_FORM_STATE, isValid = true)
            stateFactory.authenticating(VALID_FORM_STATE)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun doesNotProcessActionIfInvalid() = test {
        state.start(stateMachine)
        state.process(AuthGestureImpl.Action)

        verify(VerifyMode.exactly(0)) {
            stateFactory.authenticating(any())
            stateMachine.setMachineState(any())
        }
    }

    @Test
    fun processesBack() = test {
        every { stateFactory.terminated(any()) } returns nextState

        state.start(stateMachine)
        state.process(AuthGestureImpl.Back)

        verify {
            stateFactory.terminated(AuthResult(authenticated = false))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun processesSkip() = test {
        every { stateFactory.terminated(any()) } returns nextState

        state.start(stateMachine)
        state.process(AuthGestureImpl.Form.Skip)

        verify {
            stateFactory.terminated(AuthResult(authenticated = false))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun doesNotProcessSkipIfNotSkippable() = test {
        state.data = EMPTY_STATE.copy(input = EMPTY_STATE.input.copy(skippable = false))

        state.start(stateMachine)
        state.process(AuthGestureImpl.Form.Skip)

        verify(VerifyMode.exactly(0)) {
            stateFactory.terminated(any())
            stateMachine.setMachineState(any())
        }
    }
}
