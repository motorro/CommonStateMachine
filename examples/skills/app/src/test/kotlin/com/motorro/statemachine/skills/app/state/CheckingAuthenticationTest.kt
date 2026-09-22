package com.motorro.statemachine.skills.app.state

import com.motorro.commonstatemachine.skills.domain.session.SessionManager
import com.motorro.commonstatemachine.skills.domain.session.data.Session
import com.motorro.commonstatemachine.skills.domain.session.data.Username
import com.motorro.statemachine.skills.app.data.MainGesture
import com.motorro.statemachine.skills.app.data.MainUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.suspendCancellableCoroutine
import org.junit.Test

internal class CheckingAuthenticationTest : BaseStateTest() {
    private lateinit var sessionManager: SessionManager
    private lateinit var state: CheckingAuthentication

    private fun createState(sessionFlow: Flow<Session>) {
        sessionManager = mockk {
            every { session } returns sessionFlow
        }
        state = CheckingAuthentication(context, sessionManager)
    }

    @Test
    fun displaysSplashOnStart() = test {
        createState(flowOf(Session.None))
        every { renderer.renderSplash() } returns MainUiState.Splash
        every { stateFactory.auth() } returns nextState

        state.start(stateMachine)

        verify {
            renderer.renderSplash()
            stateMachine.setUiState(MainUiState.Splash)
        }
    }

    @Test
    fun transfersToContentWhenAuthenticated() = test {
        createState(flowOf(Session.Active(Username("user"))))
        every { renderer.renderSplash() } returns MainUiState.Splash
        every { stateFactory.content() } returns nextState

        state.start(stateMachine)

        verify {
            stateFactory.content()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToAuthWhenNotAuthenticated() = test {
        createState(flowOf(Session.None))
        every { renderer.renderSplash() } returns MainUiState.Splash
        every { stateFactory.auth() } returns nextState

        state.start(stateMachine)

        verify {
            stateFactory.auth()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToAuthOnSessionError() = test {
        createState(flow { throw RuntimeException("Error") })
        every { renderer.renderSplash() } returns MainUiState.Splash
        every { stateFactory.auth() } returns nextState

        state.start(stateMachine)

        verify {
            stateFactory.auth()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun terminatesOnBack() = test {
        createState(
            flow {
                suspendCancellableCoroutine {
                    // No-op
                }
            }
        )
        every { renderer.renderSplash() } returns MainUiState.Splash
        every { stateFactory.terminated() } returns nextState

        state.start(stateMachine)
        state.process(MainGesture.Back)

        verify {
            stateFactory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}
