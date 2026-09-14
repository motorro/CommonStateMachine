package com.motorro.statemachine.auth.implementation.state

import com.motorro.commonstatemachine.flow.data.CommonFlowHost
import com.motorro.statemachine.auth.api.AuthResult
import com.motorro.statemachine.auth.implementation.ui.AuthUiRenderer

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