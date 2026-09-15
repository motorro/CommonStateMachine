package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.commonstatemachine.skills.domain.usecase.GetPasswordRequirements
import com.motorro.statemachine.skills.auth.api.AuthResult
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.FATAL_ERROR
import com.motorro.statemachine.skills.auth.implementation.data.INPUT
import com.motorro.statemachine.skills.auth.implementation.data.PASSWORD_REQUIREMENTS
import com.motorro.statemachine.skills.auth.implementation.data.UI_STATE
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.test.Test

internal class PreloadingStateTest : BaseStateTest() {
    private lateinit var requirementsMock: GetPasswordRequirements
    private lateinit var state: PreloadingState

    override fun doInit() {
        requirementsMock = mock()
        state = PreloadingState(context, INPUT, requirementsMock)
        every { renderer.renderLoading() } returns UI_STATE
    }

    @Test
    fun rendersLoadingOnStart() = test {
        everySuspend { requirementsMock() } calls { suspendCancellableCoroutine { } }

        state.start(stateMachine)

        verify {
            renderer.renderLoading()
            stateMachine.setUiState(UI_STATE)
        }
    }

    @Test
    fun transfersToFormWhenLoaded() = test {
        everySuspend { requirementsMock() } returns PASSWORD_REQUIREMENTS
        every { stateFactory.form(any()) } returns nextState

        state.start(stateMachine)

        verify {
            stateFactory.form(any())
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToErrorWhenFailed() = test {
        everySuspend { requirementsMock() } throws FATAL_ERROR
        every { stateFactory.preloadError(any(), any()) } returns nextState

        state.start(stateMachine)

        verify {
            stateFactory.preloadError(INPUT, any())
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun terminatesOnBackWhileLoading() = test {
        everySuspend { requirementsMock() } calls { suspendCancellableCoroutine { } }
        every { stateFactory.terminated(any()) } returns nextState

        state.start(stateMachine)
        state.process(AuthGestureImpl.Back)

        verify {
            stateFactory.terminated(AuthResult(authenticated = false))
            stateMachine.setMachineState(nextState)
        }
    }
}
