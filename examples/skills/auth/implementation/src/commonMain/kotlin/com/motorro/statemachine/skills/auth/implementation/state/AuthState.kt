package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.AuthUiStateImpl

/**
 * Convenient type-alias - bounds gesture and ui state system
 */
internal typealias AuthState = CommonMachineState<AuthGestureImpl, AuthUiStateImpl>