package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.login.R
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.login.di.LoginScope
import com.motorro.statemachine.resources.ResourceWrapper
import timber.log.Timber
import javax.inject.Inject

/**
 * Login error
 */
class ErrorState(
    context: LoginContext,
    private val data: LoginDataState,
    private val error: Throwable,
    private val resourceWrapper: ResourceWrapper
) : LoginState(context), ResourceWrapper by resourceWrapper {

    /**
     * Should have valid email at this point
     */
    private val email = requireNotNull(data.commonData.email) {
        "Email is not provided"
    }

    /**
     * Should have password at this point
     */
    private val password = requireNotNull(data.password) {
        "Password is not provided"
    }

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
        setUiState(
            LoginUiState.LoginError(
                renderPassword(email, password, false),
                getErrorMessage()
            )
        )
    }

    private fun getErrorMessage(): String = when(error) {
        is IllegalArgumentException -> getString(R.string.login_error_credentials)
        else -> getString(R.string.login_error_unknown)
    }

    @LoginScope
    class Factory @Inject constructor(private val resourceWrapper: ResourceWrapper) {
        operator fun invoke(
            context: LoginContext,
            data: LoginDataState,
            error: Throwable
        ): LoginState = ErrorState(
            context,
            data,
            error,
            resourceWrapper
        )
    }
}