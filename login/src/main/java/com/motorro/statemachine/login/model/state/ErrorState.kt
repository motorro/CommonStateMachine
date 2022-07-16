package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import timber.log.Timber

/**
 * Login error
 */
class ErrorState(
    context: LoginContext,
    private val data: LoginDataState,
    private val error: Throwable
) : LoginState(context) {

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) = when(gesture) {
        LoginGesture.Back, LoginGesture.Action -> onDismiss()
        else -> super.doProcess(gesture)
    }

    private fun onDismiss() {
        Timber.d("Returning to password entry")
        setMachineState(factory.passwordEntry(data))
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(renderer.renderError(data, error))
    }
}