package com.motorro.statemachine.login.model.state

import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
import com.motorro.statemachine.login.model.LoginRenderer


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

    /**
     * UI-state renderer
     */
    val renderer: LoginRenderer
}