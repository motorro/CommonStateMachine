package com.motorro.commonstatemachine.examples.welcome.login

import com.motorro.commonstatemachine.examples.welcome.commonapi.model.state.FlowStarter
import com.motorro.commonstatemachine.examples.welcome.login.data.LoginGesture
import com.motorro.commonstatemachine.examples.welcome.login.data.LoginUiState

/**
 * Starts login flow
 */
interface LoginFlowStarter : FlowStarter<LoginGesture, LoginUiState>