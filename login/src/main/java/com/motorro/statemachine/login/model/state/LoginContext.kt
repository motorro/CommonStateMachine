package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.commonapi.model.state.RegistrationFeatureHost


/**
 * Context for login flow
 */
interface LoginContext {
    /**
     * State factory
     */
    val factory: LoginStateFactory

    /**
     * Flow host
     */
    val host: RegistrationFeatureHost
}