package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.commonstatemachine.skills.domain.authenticate.AuthenticateWithPassword
import com.motorro.statemachine.skills.auth.api.AuthResult
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.FATAL_ERROR
import com.motorro.statemachine.skills.auth.implementation.data.UI_STATE
import com.motorro.statemachine.skills.auth.implementation.data.VALID_FORM_STATE
import dev.mokkery.MockMode
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

internal class AuthenticatingStateTest : BaseStateTest() {
    private lateinit var authenticateMock: AuthenticateWithPassword
    private lateinit var state: AuthenticatingState

    override fun doInit() {
        authenticateMock = mock(MockMode.strict)
        state = AuthenticatingState(context, VALID_FORM_STATE, authenticateMock)
        every { renderer.renderLoading() } returns UI_STATE
    }

    @Test
    fun rendersLoadingOnStart() = test {
        everySuspend { authenticateMock(any(), any()) } calls { suspendCancellableCoroutine { } }

        state.start(stateMachine)

        verify {
            renderer.renderLoading()
            stateMachine.setUiState(UI_STATE)
        }
    }

    @Test
    fun terminatesWhenAuthenticated() = test {
        everySuspend { authenticateMock(any(), any()) } returns Unit
        every { stateFactory.terminated(any()) } returns nextState

        state.start(stateMachine)

        verify {
            stateFactory.terminated(AuthResult(authenticated = true))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToErrorWhenFailed() = test {
        everySuspend { authenticateMock(any(), any()) } throws FATAL_ERROR
        every { stateFactory.authenticationError(any(), any()) } returns nextState

        state.start(stateMachine)

        verify {
            stateFactory.authenticationError(VALID_FORM_STATE, any())
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun returnsToFormOnBackWhileAuthenticating() = test {
        everySuspend { authenticateMock(any(), any()) } calls { suspendCancellableCoroutine { } }
        every { stateFactory.form(any()) } returns nextState

        state.start(stateMachine)
        state.process(AuthGestureImpl.Back)

        verify {
            stateFactory.form(VALID_FORM_STATE)
            stateMachine.setMachineState(nextState)
        }
    }
}
