package com.motorro.statemachine.welcome.model.state

import androidx.lifecycle.SavedStateHandle

/**
 * Common state passed from state to state
 */
interface WelcomeContext {
    /**
     * State factory
     */
    val factory: WelcomeStateFactory

    /**
     * Saved state handle
     */
    val savedStateHandle: SavedStateHandle
}