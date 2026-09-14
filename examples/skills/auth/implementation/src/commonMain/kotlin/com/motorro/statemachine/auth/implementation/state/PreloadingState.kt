package com.motorro.statemachine.auth.implementation.state

import com.motorro.statemachine.auth.api.AuthInput
import com.motorro.statemachine.auth.api.AuthResult
import com.motorro.statemachine.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.auth.implementation.data.AuthStateData
import com.motorro.statemachine.auth.implementation.usecase.GetPasswordRequirements
import io.github.aakira.napier.Napier
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

/**
 * Example of a state that preloads some data with the usecase
 * and creates the valid [data-state](data-and-ui-implementation.md#step-3-interstate-data).
 */
internal class PreloadingState(
    context: AuthContext,
    private val input: AuthInput,
    private val getPasswordRequirements: GetPasswordRequirements
) : BaseAuthState(context) {

    /**
     * State start-up
     */
    override fun doStart() {
        setUiState {
            renderLoading()
        }
        preload()
    }

    /**
     * Dummy gesture processing
     */
    override fun doProcess(gesture: AuthGestureImpl) {
        when(gesture) {
            is AuthGestureImpl.Back -> setMachineState {
                terminated(AuthResult(false))
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * Dummy preloading function
     */
    private fun preload() = stateScope.launch {
        try {
            val passwordRequirements = getPasswordRequirements()
            // Advance to the next state
            setMachineState {
                form(AuthStateData(input, passwordRequirements))
            }
        } catch (e: Throwable) {
            ensureActive()
            Napier.w(e) { "Error during initialization" }
            // Advance to the error state
            setMachineState {
                preloadError(input, e)
            }
        }
    }

    /**
     * State factory
     * State dependencies are injected here
     */
    class Factory(private val getPasswordRequirements: GetPasswordRequirements) {
        fun create(context: AuthContext, input: AuthInput): AuthState = PreloadingState(
            context = context,
            input = input,
            getPasswordRequirements = getPasswordRequirements
        )
    }
}