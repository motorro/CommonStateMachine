package com.motorro.statemachine.skills.auth.api

/**
 * A base type for authentication UI state
 * Available to a feature consumer
 */
interface AuthUiState {
    /**
     * Some common property available to a feature consumer
     */
    val interactive: Boolean
}