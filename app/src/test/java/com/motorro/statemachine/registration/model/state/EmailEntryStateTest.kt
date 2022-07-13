package com.motorro.statemachine.registration.model.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.registrationapi.data.RegistrationDataState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

internal class EmailEntryStateTest : BaseStateTest() {

    private fun createState(data: RegistrationDataState? = RegistrationDataState()) = EmailEntryState(
        context,
        data
    )

    @Test
    fun displaysNonValidStateOnEmptyEmail() {
        val state = createState()

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                RegistrationUiState.EmailEntry("", false)
            )
        }
    }

    @Test
    fun displaysValidStateOnValidEmail() {
        val email = "test@example.com"
        val state = createState(RegistrationDataState(email))

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                RegistrationUiState.EmailEntry(email, true)
            )
        }
    }

    @Test
    fun restoresEmailFromSavedState() {
        val email = "test@example.com"
        val saved = SavedStateHandle(mapOf(EmailEntryState.EMAIL_KEY to email))
        every { context.savedStateHandle } returns saved
        val state = createState(null)

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                RegistrationUiState.EmailEntry(email, true)
            )
        }
    }

    @Test
    fun updatesEmail() {
        val email = "test@example.com"
        val state = createState()

        state.start(stateMachine)
        state.process(RegistrationGesture.EmailChanged(email))

        verifyOrder {
            stateMachine.setUiState(
                RegistrationUiState.EmailEntry("", false)
            )
            stateMachine.setUiState(
                RegistrationUiState.EmailEntry(email, true)
            )
        }
    }

    @Test
    fun transfersToCheckIfValid() {
        val email = "test@example.com"
        val data = RegistrationDataState(email = email)
        val state = createState(data)
        val checking: RegistrationState = mockk()
        every { factory.checkEmail(any()) } returns checking

        state.start(stateMachine)
        state.process(RegistrationGesture.Action)

        verify { stateMachine.setMachineState(checking) }
        verify { factory.checkEmail(data) }
    }

    @Test
    fun doesNotTransferToCheckIfNotValid() {
        val email = ""
        val data = RegistrationDataState(email = email)
        val state = createState(data)

        state.start(stateMachine)
        state.process(RegistrationGesture.Action)

        verify(exactly = 0) { stateMachine.setMachineState(any()) }
        verify(exactly = 0) { factory.checkEmail(data) }
    }

    @Test
    fun terminatesOnBack() {
        val terminated: RegistrationState = mockk()
        every { factory.terminate() } returns terminated
        val state = createState()

        state.start(stateMachine)
        state.process(RegistrationGesture.Back)

        verify { stateMachine.setMachineState(terminated) }
        verify { factory.terminate() }
    }
}