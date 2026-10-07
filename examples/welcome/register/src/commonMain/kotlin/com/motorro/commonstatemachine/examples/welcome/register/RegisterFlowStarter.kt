package com.motorro.commonstatemachine.examples.welcome.register

import com.motorro.commonstatemachine.examples.welcome.commonapi.model.state.FlowStarter
import com.motorro.commonstatemachine.examples.welcome.register.data.RegisterGesture
import com.motorro.commonstatemachine.examples.welcome.register.data.RegisterUiState

/**
 * Starts login flow
 */
interface RegisterFlowStarter : FlowStarter<RegisterGesture, RegisterUiState>