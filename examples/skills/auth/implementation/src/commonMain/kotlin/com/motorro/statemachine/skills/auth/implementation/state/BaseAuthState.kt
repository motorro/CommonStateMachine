package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.AuthUiStateImpl
import com.motorro.statemachine.skills.auth.implementation.ui.AuthUiRenderer
import io.github.aakira.napier.Napier

/**
 * Base class for the most logic states
 */
internal abstract class BaseAuthState(context: AuthContext) : CoroutineState<AuthGestureImpl, AuthUiStateImpl>(), AuthContext by context {

    /**
     * Common gesture processing (logs a warning for an unexpected gesture)
     */
    override fun doProcess(gesture: AuthGestureImpl) {
        Napier.w { "Unsupported gesture: $gesture" }
    }

    /**
     * Convenience function to switch a machine state
     */
    protected inline fun setMachineState(block: AuthStateFactory.() -> AuthState) {
        setMachineState(block(factory))
    }

    /**
     * Convenience function to render UI state
     */
    protected inline fun setUiState(block: AuthUiRenderer.() -> AuthUiStateImpl) {
        setUiState(renderer.block())
    }
}