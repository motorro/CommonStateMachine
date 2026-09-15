package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.commonstatemachine.flow.data.CommonFlowHost
import com.motorro.commonstatemachine.skills.domain.exception.AppException
import com.motorro.statemachine.skills.auth.api.AuthInput
import com.motorro.statemachine.skills.auth.api.AuthResult
import com.motorro.statemachine.skills.auth.implementation.data.AuthStateData
import com.motorro.statemachine.skills.auth.implementation.ui.AuthUiRenderer
import io.github.aakira.napier.Napier

/**
 * Main state-factory implementation
 */
internal class AuthStateFactoryImpl(
    private val preloadingStateFactory: PreloadingState.Factory,
    flowHost: CommonFlowHost<AuthResult>,
    renderer: AuthUiRenderer
) : AuthStateFactory {

    /**
     * Context implementation
     */
    private val context = object : AuthContext {
        override val factory: AuthStateFactory = this@AuthStateFactoryImpl
        override val flowHost: CommonFlowHost<AuthResult> = flowHost
        override val renderer: AuthUiRenderer = renderer
    }

    /**
     * Creates preloading state with help of a preloading state factory
     */
    override fun preloading(input: AuthInput) = preloadingStateFactory.create(
        context,
        input
    )

    /**
     * Creates preload error state directly
     */
    override fun preloadError(input: AuthInput, error: AppException) = PreloadingErrorState(
        context,
        input,
        error
    )

    /**
     * Creates authentication form state
     */
    override fun form(data: AuthStateData) = FormState(
        context,
        data
    )

    /**
     * Left for the future
     */
    override fun authenticating(data: AuthStateData): AuthState {
        TODO("Implement the authenticating state")
    }

    /**
     * Ad-hoc terminated state implementation
     */
    override fun terminated(result: AuthResult) = object : AuthState() {
        override fun doStart() {
            Napier.d { "Terminating flow..." }
            context.flowHost.onComplete(result)
        }
    }
}