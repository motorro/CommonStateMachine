package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.commonstatemachine.skills.domain.exception.toAppException
import com.motorro.commonstatemachine.skills.domain.usecase.AuthenticateWithPassword
import com.motorro.statemachine.skills.auth.api.AuthResult
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.AuthStateData
import io.github.aakira.napier.Napier
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

/**
 * Authenticates the user
 * @param context Auth context
 * @param data Interstate data
 * @param authenticate Authenticate usecase
 */
internal class AuthenticatingState(
    context: AuthContext,
    private val data: AuthStateData,
    private val authenticate: AuthenticateWithPassword
) : BaseAuthState(context) {

    /**
     * State start-up
     */
    override fun doStart() {
        setUiState {
            renderLoading()
        }
        doAuthenticate()
    }

    /**
     * Gesture processing
     */
    override fun doProcess(gesture: AuthGestureImpl) {
        when(gesture) {
            is AuthGestureImpl.Back -> setMachineState {
                Napier.d { "Back gesture. Returning to form..." }
                form(data)
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * Authentication function
     */
    private fun doAuthenticate() = stateScope.launch {
        try {
            authenticate(data.username, data.password)
            // Advance to the next state
            setMachineState {
                terminated(AuthResult(authenticated = true))
            }
        } catch (e: Throwable) {
            ensureActive()
            Napier.w(e) { "Error during authentication" }
            // Advance to the error state
            setMachineState {
                authenticationError(data, e.toAppException())
            }
        }
    }

    /**
     * State factory
     * State dependencies are injected here
     */
    class Factory(private val authenticate: AuthenticateWithPassword) {
        fun create(context: AuthContext, data: AuthStateData): AuthState = AuthenticatingState(
            context = context,
            data = data,
            authenticate = authenticate
        )
    }
}
