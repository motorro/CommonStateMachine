package com.motorro.statemachine.commonapi.welcome.model.state

import com.motorro.statemachine.commonapi.welcome.data.WelcomeDataState


/**
 * A host for welcome flow feature that authenticates users
 */
interface WelcomeFeatureHost {
    /**
     * Returns user to email entry screen
     * @param data Common registration state data
     */
    fun backToEmailEntry(data: WelcomeDataState)

    /**
     * Authentication complete
     * @param email Authenticated user's email
     */
    fun complete(email: String)
}