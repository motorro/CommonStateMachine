package com.motorro.commonstatemachine.examples.skills.auth.implementation.state

import com.motorro.commonstatemachine.examples.skills.auth.api.AuthResult
import com.motorro.commonstatemachine.examples.skills.auth.implementation.ui.AuthUiRenderer
import com.motorro.commonstatemachine.flow.data.CommonFlowHost

/**
 * Common tools used by states
 */
internal interface AuthContext {
    /**
     * State-factory
     */
    val factory: AuthStateFactory

    /**
     * Flow-host
     */
    val flowHost: CommonFlowHost<AuthResult>

    /**
     * UI-renderer
     */
    val renderer: AuthUiRenderer
}