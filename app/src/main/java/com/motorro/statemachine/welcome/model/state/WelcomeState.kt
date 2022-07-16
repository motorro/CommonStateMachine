package com.motorro.statemachine.welcome.model.state

import com.motorro.commonstatemachine.CoroutineState
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import timber.log.Timber

/**
 * Base class for welcome flow state
 */
abstract class WelcomeState(
    context: WelcomeContext
): CoroutineState<WelcomeGesture, WelcomeUiState>(), WelcomeContext by context {
    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: WelcomeGesture) {
        Timber.w("Unsupported gesture: %s", gesture)
    }
}