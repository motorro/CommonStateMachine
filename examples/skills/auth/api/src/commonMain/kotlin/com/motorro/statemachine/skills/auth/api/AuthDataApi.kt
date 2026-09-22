package com.motorro.statemachine.skills.auth.api
import com.motorro.commonstatemachine.flow.data.CommonFlowDataApi

/**
 * Data API used to run a child-flow in a proxy machine
 * Available to a feature consumer
 */
interface AuthDataApi : CommonFlowDataApi<AuthGesture, AuthUiState, AuthInput, AuthResult>