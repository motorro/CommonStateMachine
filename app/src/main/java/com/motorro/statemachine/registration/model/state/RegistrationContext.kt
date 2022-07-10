package com.motorro.statemachine.registration.model.state

/**
 * Common state passed from state to state
 */
interface RegistrationContext {
    /**
     * State factory
     */
    val factory: RegistrationStateFactory
}