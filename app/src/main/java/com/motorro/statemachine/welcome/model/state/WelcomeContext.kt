package com.motorro.statemachine.welcome.model.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.statemachine.welcome.model.WelcomeRenderer

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

    /**
     * Renderer
     */
    val renderer: WelcomeRenderer
}