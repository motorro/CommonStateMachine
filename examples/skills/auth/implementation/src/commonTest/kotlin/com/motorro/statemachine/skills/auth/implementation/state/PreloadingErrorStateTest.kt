package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.commonstatemachine.skills.domain.exception.AppException
import com.motorro.statemachine.skills.auth.api.AuthResult
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.FATAL_ERROR
import com.motorro.statemachine.skills.auth.implementation.data.INPUT
import com.motorro.statemachine.skills.auth.implementation.data.NON_FATAL_ERROR
import com.motorro.statemachine.skills.auth.implementation.data.UI_STATE
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.verify
import kotlin.test.Test

internal class PreloadingErrorStateTest : BaseStateTest() {
    override fun doInit() {
        every { renderer.renderFullScreenError(any(), any()) } returns UI_STATE
    }

    private fun createState(error: AppException) = PreloadingErrorState(
        context,
        INPUT,
        error
    )

    @Test
    fun rendersFatalErrorOnStart() = test {
        val state = createState(FATAL_ERROR)
        state.start(stateMachine)
        verify {
            renderer.renderFullScreenError(FATAL_ERROR.message, canRetry = false)
            stateMachine.setUiState(UI_STATE)
        }
    }

    @Test
    fun rendersNonFatalErrorOnStart() = test {
        val state = createState(NON_FATAL_ERROR)
        state.start(stateMachine)
        verify {
            renderer.renderFullScreenError(NON_FATAL_ERROR.message, canRetry = true)
            stateMachine.setUiState(UI_STATE)
        }
    }

    @Test
    fun terminatesOnBack() = test {
        val state = createState(NON_FATAL_ERROR)

        every { stateFactory.terminated(any()) } returns nextState

        state.start(stateMachine)
        state.process(AuthGestureImpl.Back)

        verify {
            stateFactory.terminated(AuthResult(authenticated = false))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun terminatesOnActionIfCannotRetry() = test {
        val state = createState(FATAL_ERROR)

        every { stateFactory.terminated(any()) } returns nextState

        state.start(stateMachine)
        state.process(AuthGestureImpl.Action)

        verify {
            stateFactory.terminated(AuthResult(authenticated = false))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun reloadsOnActionIfCanRetry() = test {
        val state = createState(NON_FATAL_ERROR)

        every { stateFactory.preloading(any()) } returns nextState

        state.start(stateMachine)
        state.process(AuthGestureImpl.Action)

        verify {
            stateFactory.preloading(INPUT)
            stateMachine.setMachineState(nextState)
        }
    }
}
