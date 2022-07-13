package com.motorro.statemachine.registrationapi.model.state

import com.motorro.statemachine.registrationapi.data.RegistrationDataState

/**
 * A host for registration flow feature that authenticates users
 */
interface RegistrationFeatureHost {
    /**
     * Returns user to email entry screen
     * @param data Common registration state data
     */
    fun backToEmailEntry(data: RegistrationDataState)

    /**
     * Authentication complete
     * @param email Authenticated user's email
     */
    fun complete(email: String)
}