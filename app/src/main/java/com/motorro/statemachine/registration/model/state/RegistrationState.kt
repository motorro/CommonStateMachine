package com.motorro.statemachine.registration.model.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.statemachine.machine.CoroutineState
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import timber.log.Timber

/**
 * Base class for registration flow state
 */
abstract class RegistrationState(private val context: RegistrationContext): CoroutineState<RegistrationGesture, RegistrationUiState>() {
    /**
     * State factory
     */
    protected val factory: RegistrationStateFactory
        get() = context.factory

    /**
     * Saved state handle
     */
    protected val savedStateHandle: SavedStateHandle
        get() = context.savedStateHandle

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: RegistrationGesture) {
        Timber.w("Unsupported gesture: %s", gesture)
    }
}