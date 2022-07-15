package com.motorro.statemachine.welcome.model.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.statemachine.welcome.data.WelcomeDataState
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

internal class EmailEntryStateTest : BaseStateTest() {

    private fun createState(data: WelcomeDataState? = WelcomeDataState()) = EmailEntryState(
        context,
        data
    )

    @Test
    fun displaysNonValidStateOnEmptyEmail() {
        val state = createState()

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                WelcomeUiState.EmailEntry("", false)
            )
        }
    }

    @Test
    fun displaysValidStateOnValidEmail() {
        val email = "test@example.com"
        val state = createState(WelcomeDataState(email))

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                WelcomeUiState.EmailEntry(email, true)
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
                WelcomeUiState.EmailEntry(email, true)
            )
        }
    }

    @Test
    fun updatesEmail() {
        val email = "test@example.com"
        val state = createState()

        state.start(stateMachine)
        state.process(WelcomeGesture.EmailChanged(email))

        verifyOrder {
            stateMachine.setUiState(
                WelcomeUiState.EmailEntry("", false)
            )
            stateMachine.setUiState(
                WelcomeUiState.EmailEntry(email, true)
            )
        }
    }

    @Test
    fun transfersToCheckIfValid() {
        val email = "test@example.com"
        val data = WelcomeDataState(email = email)
        val state = createState(data)
        val checking: WelcomeState = mockk()
        every { factory.checkEmail(any()) } returns checking

        state.start(stateMachine)
        state.process(WelcomeGesture.Action)

        verify { stateMachine.setMachineState(checking) }
        verify { factory.checkEmail(data) }
    }

    @Test
    fun doesNotTransferToCheckIfNotValid() {
        val email = ""
        val data = WelcomeDataState(email = email)
        val state = createState(data)

        state.start(stateMachine)
        state.process(WelcomeGesture.Action)

        verify(exactly = 0) { stateMachine.setMachineState(any()) }
        verify(exactly = 0) { factory.checkEmail(data) }
    }

    @Test
    fun terminatesOnBack() {
        val terminated: WelcomeState = mockk()
        every { factory.terminate() } returns terminated
        val state = createState()

        state.start(stateMachine)
        state.process(WelcomeGesture.Back)

        verify { stateMachine.setMachineState(terminated) }
        verify { factory.terminate() }
    }
}