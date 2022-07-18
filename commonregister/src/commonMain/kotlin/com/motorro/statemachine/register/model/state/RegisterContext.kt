package com.motorro.statemachine.register.model.state

import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
import com.motorro.statemachine.register.model.RegistrationRenderer


/**
 * Context for login flow
 */
interface RegisterContext {
    /**
     * State factory
     */
    val factory: RegisterStateFactory

    /**
     * Flow host
     */
    val host: WelcomeFeatureHost

    /**
     * Renderer
     */
    val renderer: RegistrationRenderer
}