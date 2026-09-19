package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.commonstatemachine.skills.domain.exception.AppException
import com.motorro.commonstatemachine.skills.domain.exception.IOException
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.AuthStateData
import io.github.aakira.napier.Napier

/**
 * Handles authentication error
 * If the error is recoverable (IOException), allows to transfer back to authenticating to retry.
 * Otherwise returns to form.
 * @param context Auth context
 * @param data Interstate data
 * @param error Error to handle
 */
internal class AuthenticationErrorState(
    context: AuthContext,
    private val data: AuthStateData,
    private val error: AppException
) : BaseAuthState(context) {
    override fun doStart() {
        setUiState {
            renderFullScreenError(error, canRetry())
        }
    }

    private fun canRetry(): Boolean = when(error) {
        is IOException -> true
        else -> false
    }

    override fun doProcess(gesture: AuthGestureImpl) {
        when(gesture) {
            is AuthGestureImpl.Back -> setMachineState {
                Napier.d { "Back gesture. Returning to form..." }
                form(data)
            }
            is AuthGestureImpl.Action -> if (canRetry()) {
                Napier.d { "Action gesture. Retrying authentication..." }
                setMachineState {
                    authenticating(data)
                }
            } else {
                Napier.d { "Action gesture. Returning to form..." }
                setMachineState {
                    form(data)
                }
            }
            else -> super.doProcess(gesture)
        }
    }
}
