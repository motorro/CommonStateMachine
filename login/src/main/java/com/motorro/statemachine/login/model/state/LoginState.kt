package com.motorro.statemachine.login.model.state

import com.motorro.commonstatemachine.CoroutineState
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.welcome.model.state.WelcomeFeatureHost
import timber.log.Timber

/**
 * Base class for login flow state
 */
abstract class LoginState(private val context: LoginContext): CoroutineState<LoginGesture, LoginUiState>() {
    /**
     * State factory
     */
    protected val factory: LoginStateFactory
        get() = context.factory

    /**
     * Flow host
     */
    protected val host: WelcomeFeatureHost
        get() = context.host

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) {
        Timber.w("Unsupported gesture: %s", gesture)
    }
}