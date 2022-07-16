package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import timber.log.Timber

/**
 * Password entry screen
 */
class PasswordEntryState(
    context: LoginContext,
    private var data: LoginDataState
) : LoginState(context) {

    /**
     * Should have valid email at this point
     */
    private val email = requireNotNull(data.commonData.email) {
        "Email is not provided"
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        render()
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) = when(gesture) {
        LoginGesture.Action -> onAction()
        LoginGesture.Back -> onBack()
        is LoginGesture.PasswordChanged -> onPasswordChanged(gesture)
    }

    private fun onAction() {
        if (isPasswordValid()) {
            Timber.d("Valid password. Transferring to credentials check")
            setMachineState(factory.checking(data))
        }
    }

    private fun onBack() {
        Timber.d("Returning to e-mail entry...")
        host.backToEmailEntry(data.commonData)
    }

    private fun onPasswordChanged(gesture: LoginGesture.PasswordChanged) {
        data = data.copy(password = gesture.value)
        render()
    }

    private fun render() {
        setUiState(renderer.renderPassword(data, isPasswordValid()))
    }

    private fun isPasswordValid(): Boolean = null != data.password?.takeIf { it.length >= 6 }
}

