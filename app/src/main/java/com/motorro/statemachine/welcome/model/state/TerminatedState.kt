package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.welcome.data.WelcomeUiState
import timber.log.Timber

/**
 * Terminates wizard
 */
class TerminatedState(context: WelcomeContext) : WelcomeState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Timber.d("Terminated")
        setUiState(WelcomeUiState.Terminated)
    }
}