package com.motorro.statemachine.registration.model.state

import com.motorro.statemachine.registration.data.RegistrationDataState

/**
 * Registration state factory
 */
interface RegistrationStateFactory {
    /**
     * Creates welcome screen state
     */
    fun welcome(): RegistrationState

    /**
     * Creates email-entry state
     * @param data Data state
     */
    fun emailEntry(data: RegistrationDataState? = null): RegistrationState

    /**
     * Terminates registration flow
     */
    fun terminate() : RegistrationState
}