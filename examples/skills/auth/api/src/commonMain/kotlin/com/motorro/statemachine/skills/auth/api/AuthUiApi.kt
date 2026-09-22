package com.motorro.statemachine.skills.auth.api
import com.motorro.commonstatemachine.flow.compose.CommonFlowUiApi

/**
 * UI API used display a child flow inside a parent composition
 * Available to a feature consumer
 */
interface AuthUiApi: CommonFlowUiApi<AuthGesture, AuthUiState>