package com.motorro.statemachine.registration.model.state

import androidx.lifecycle.SavedStateHandle

/**
 * Common state passed from state to state
 */
interface RegistrationContext {
    /**
     * State factory
     */
    val factory: RegistrationStateFactory

    /**
     * Saved state handle
     */
    val savedStateHandle: SavedStateHandle
}