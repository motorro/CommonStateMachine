package com.motorro.statemachine.auth.api
import com.motorro.commonstatemachine.flow.data.CommonFlowDataApi

/**
 * Data API used to run a child-flow in a proxy machine
 * Available to a feature consumer
 */
typealias AuthDataApi = CommonFlowDataApi<AuthGesture, AuthUiState, AuthInput, AuthResult>