package com.motorro.statemachine.auth.implementation.state

import com.motorro.statemachine.auth.api.AuthResult
import com.motorro.statemachine.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.auth.implementation.data.AuthStateData
import com.motorro.statemachine.auth.implementation.data.isValidToAuthenticate
import io.github.aakira.napier.Napier
import org.jetbrains.annotations.VisibleForTesting

/**
 * Authentication form state - an interactive state that:
 * - handles user input
 * - updates the UI state
 * - validates the input
 * - updates the interstate data
 * @param context Auth context
 * @param data Interstate data. Visible for testing.
 */
internal class FormState(
    context: AuthContext,
    @VisibleForTesting var data: AuthStateData
) : BaseAuthState(context) {

    /**
     * State start-up
     */
    override fun doStart() {
        setUiState {
            renderForm(data, data.isValidToAuthenticate())
        }
    }

    /**
     * Updates the interstate data and renders the form
     */
    private inline fun updateData(block: AuthStateData.() -> AuthStateData) {
        val oldData = data
        val newData = data.block()
        if (oldData != newData) {
            data = newData
            setUiState {
                renderForm(newData, newData.isValidToAuthenticate())
            }
        }
    }

    /**
     * Processes the gesture
     */
    override fun doProcess(gesture: AuthGestureImpl) {
        when(gesture) {
            is AuthGestureImpl.Back -> setMachineState {
                terminated(AuthResult(false))
            }
            is AuthGestureImpl.Action -> if (data.isValidToAuthenticate()) {
                Napier.d { "Action gesture. Authenticating..." }
                setMachineState {
                    authenticating(data)
                }
            }
            is AuthGestureImpl.Form.UsernameChanged -> updateData {
                copy(username = gesture.value)
            }
            is AuthGestureImpl.Form.PasswordChanged -> updateData {
                copy(password = gesture.value)
            }
        }
    }
}