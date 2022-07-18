@file:Suppress("RemoveExplicitTypeArguments")

package com.motorro.statemachine.register.model.state

import com.motorro.statemachine.commonapi.welcome.data.GOOD
import com.motorro.statemachine.commonapi.welcome.data.WelcomeDataState
import com.motorro.statemachine.register.R_CONTENT
import com.motorro.statemachine.register.data.PasswordValidationError
import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.data.RegisterGesture
import com.motorro.statemachine.register.data.RegisterUiState
import kotlin.test.Test
import kotlin.test.assertEquals

class PasswordEntryStateTest : BaseStateTest() {
    private val password = "password"
    private val data = RegisterDataState(WelcomeDataState(GOOD))

    private lateinit var state: PasswordEntryState

    private fun createState(data: RegisterDataState = this.data) = PasswordEntryState(context, data)

    @Test
    fun displaysValidStateOnEmptyPasswordBeforeAction() {
        state = createState()
        state.start(stateMachine)

        assertEquals<List<RegisterUiState>>(
            listOf(R_CONTENT),
            stateMachine.uiStates
        )

        assertEquals<List<Triple<RegisterDataState, String?, PasswordValidationError?>>>(
            listOf(Triple(data, null, null)),
            context.renderer.passwordEntries
        )
    }

    @Test
    fun displaysNonValidStateOnEmptyPasswordAfterAction() {
        state = createState()
        state.start(stateMachine)
        state.process(RegisterGesture.Action)

        assertEquals<List<RegisterUiState>>(
            listOf(R_CONTENT, R_CONTENT),
            stateMachine.uiStates
        )

        assertEquals<List<Triple<RegisterDataState, String?, PasswordValidationError?>>>(
            listOf(
                Triple(data, null, null),
                Triple(data, null, PasswordValidationError.PASSWORD_LENGTH)
            ),
            context.renderer.passwordEntries
        )
    }

    @Test
    fun displaysNonValidStateOnEmptyRetypePasswordAfterAction() {
        val data = this.data.copy(password = password)
        state = createState(data)
        state.start(stateMachine)
        state.process(RegisterGesture.Action)

        assertEquals<List<RegisterUiState>>(
            listOf(R_CONTENT, R_CONTENT),
            stateMachine.uiStates
        )

        assertEquals<List<Triple<RegisterDataState, String?, PasswordValidationError?>>>(
            listOf(
                Triple(data, null, null),
                Triple(data, null, PasswordValidationError.PASSWORD_LENGTH)
            ),
            context.renderer.passwordEntries
        )
    }

    @Test
    fun updatesPassword() {
        state = createState(data)
        state.start(stateMachine)

        state.process(RegisterGesture.PasswordChanged(password))

        assertEquals(2, stateMachine.uiStates.size)

        assertEquals<List<Triple<RegisterDataState, String?, PasswordValidationError?>>>(
            listOf(
                Triple(data, null, null),
                Triple(data.copy(password = password), null, null),
            ),
            context.renderer.passwordEntries
        )
    }

    @Test
    fun updatesRetypePassword() {
        state = createState(data)
        state.start(stateMachine)

        state.process(RegisterGesture.RepeatPasswordChanged(password))

        assertEquals(2, stateMachine.uiStates.size)

        assertEquals<List<Triple<RegisterDataState, String?, PasswordValidationError?>>>(
            listOf(
                Triple(data, null, null),
                Triple(data, password, null),
            ),
            context.renderer.passwordEntries
        )
    }

    @Test
    fun displaysInvalidStateWhenPasswordsMismatchAfterAction() {
        val password2 = "password2"
        state = createState(data)
        state.start(stateMachine)

        state.process(RegisterGesture.PasswordChanged(password))
        state.process(RegisterGesture.RepeatPasswordChanged(password2))
        state.process(RegisterGesture.Action)

        assertEquals(4, stateMachine.uiStates.size)

        assertEquals<List<Triple<RegisterDataState, String?, PasswordValidationError?>>>(
            listOf(
                Triple(data, null, null),
                Triple(data.copy(password = password), null, null),
                Triple(data.copy(password = password), password2, null),
                Triple(data.copy(password = password), password2, PasswordValidationError.PASSWORD_MISMATCH),
            ),
            context.renderer.passwordEntries
        )
    }

    @Test
    fun transfersToRegistrationIfValid() {
        state = createState(data)
        state.start(stateMachine)

        state.process(RegisterGesture.PasswordChanged(password))
        state.process(RegisterGesture.RepeatPasswordChanged(password))
        state.process(RegisterGesture.Action)

        assertEquals(
            1,
            stateMachine.machineStates.size
        )
        assertEquals(
            listOf(data.copy(password = password)),
            factory.registers
        )
    }

    @Test
    fun doesNotTransferToRegisterIfNotValid() {
        state = createState(data)
        state.start(stateMachine)

        state.process(RegisterGesture.Action)

        assertEquals(
            0,
            stateMachine.machineStates.size
        )
        assertEquals(
            0,
            factory.registers.size
        )
    }

    @Test
    fun returnsToEmailEntryOnBack() {
        state = createState(data)
        state.start(stateMachine)

        state.process(RegisterGesture.Back)

        assertEquals(
            listOf(data.commonData),
            host.backToEmails
        )
    }
}

