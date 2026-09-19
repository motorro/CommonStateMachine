package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.commonstatemachine.skills.domain.exception.AppException
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.FATAL_ERROR
import com.motorro.statemachine.skills.auth.implementation.data.NON_FATAL_ERROR
import com.motorro.statemachine.skills.auth.implementation.data.UI_STATE
import com.motorro.statemachine.skills.auth.implementation.data.VALID_FORM_STATE
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.verify
import kotlin.test.Test

internal class AuthenticationErrorStateTest : BaseStateTest() {
    override fun doInit() {
        every { renderer.renderFullScreenError(any(), any()) } returns UI_STATE
    }

    private fun createState(error: AppException) = AuthenticationErrorState(
        context,
        VALID_FORM_STATE,
        error
    )

    @Test
    fun rendersFatalErrorOnStart() = test {
        val state = createState(FATAL_ERROR)
        state.start(stateMachine)
        verify {
            renderer.renderFullScreenError(FATAL_ERROR, canRetry = false)
            stateMachine.setUiState(UI_STATE)
        }
    }

    @Test
    fun rendersNonFatalErrorOnStart() = test {
        val state = createState(NON_FATAL_ERROR)
        state.start(stateMachine)
        verify {
            renderer.renderFullScreenError(NON_FATAL_ERROR, canRetry = true)
            stateMachine.setUiState(UI_STATE)
        }
    }

    @Test
    fun returnsToFormOnBack() = test {
        val state = createState(NON_FATAL_ERROR)
        every { stateFactory.form(any()) } returns nextState

        state.start(stateMachine)
        state.process(AuthGestureImpl.Back)

        verify {
            stateFactory.form(VALID_FORM_STATE)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun retriesOnActionIfRetryable() = test {
        val state = createState(NON_FATAL_ERROR)
        every { stateFactory.authenticating(any()) } returns nextState

        state.start(stateMachine)
        state.process(AuthGestureImpl.Action)

        verify {
            stateFactory.authenticating(VALID_FORM_STATE)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun returnsToFormOnActionIfNotRetryable() = test {
        val state = createState(FATAL_ERROR)
        every { stateFactory.form(any()) } returns nextState

        state.start(stateMachine)
        state.process(AuthGestureImpl.Action)

        verify {
            stateFactory.form(VALID_FORM_STATE)
            stateMachine.setMachineState(nextState)
        }
    }
}
