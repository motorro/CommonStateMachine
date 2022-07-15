package com.motorro.statemachine.welcome.model.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.commonstatemachine.CoroutineState
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import timber.log.Timber

/**
 * Base class for welcome flow state
 */
abstract class WelcomeState(private val context: WelcomeContext): CoroutineState<WelcomeGesture, WelcomeUiState>() {
    /**
     * State factory
     */
    protected val factory: WelcomeStateFactory
        get() = context.factory

    /**
     * Saved state handle
     */
    protected val savedStateHandle: SavedStateHandle
        get() = context.savedStateHandle

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: WelcomeGesture) {
        Timber.w("Unsupported gesture: %s", gesture)
    }
}