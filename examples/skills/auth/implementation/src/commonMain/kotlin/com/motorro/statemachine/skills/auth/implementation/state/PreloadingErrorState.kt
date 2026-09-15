package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.commonstatemachine.skills.domain.exception.AppException
import com.motorro.commonstatemachine.skills.domain.exception.IOException
import com.motorro.statemachine.skills.auth.api.AuthInput
import com.motorro.statemachine.skills.auth.api.AuthResult
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import io.github.aakira.napier.Napier

/**
 * Handles preloading error
 * If the error is recoverable, allows to transfer back to loading to retry, otherwise terminates the flow.
 * @param context Auth context
 * @param error Error to handle
 */
internal class PreloadingErrorState(
    context: AuthContext,
    private val input: AuthInput,
    private val error: AppException
) : BaseAuthState(context) {
    override fun doStart() {
        setUiState {
            renderFullScreenError(error.message, canRetry())
        }
    }

    private fun canRetry(): Boolean = when(error) {
        is IOException -> true
        else -> false
    }

    override fun doProcess(gesture: AuthGestureImpl) {
        when(gesture) {
            is AuthGestureImpl.Back -> setMachineState {
                Napier.d { "Back gesture. Terminating flow..." }
                terminated(AuthResult(false))
            }
            is AuthGestureImpl.Action -> if (canRetry()) {
                Napier.d { "Action gesture. Retrying initialization..." }
                setMachineState {
                    preloading(input)
                }
            } else {
                Napier.d { "Action gesture. Terminating flow..." }
                setMachineState {
                    terminated(AuthResult(false))
                }
            }
            else -> super.doProcess(gesture)
        }
    }
}