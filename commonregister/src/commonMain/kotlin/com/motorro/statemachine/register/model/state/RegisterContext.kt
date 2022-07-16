package com.motorro.statemachine.register.model.state

import com.motorro.statemachine.register.model.RegistrationRenderer
import com.motorro.statemachine.welcome.model.state.WelcomeFeatureHost


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