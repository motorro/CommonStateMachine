package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.welcome.model.state.WelcomeFeatureHost


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
    val host: WelcomeFeatureHost
}