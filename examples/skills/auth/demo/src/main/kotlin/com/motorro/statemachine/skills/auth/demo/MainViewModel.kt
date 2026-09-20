package com.motorro.statemachine.skills.auth.demo

import com.motorro.commonstatemachine.flow.viewmodel.CommonFlowViewModel
import com.motorro.statemachine.skills.auth.api.AuthDataApi
import com.motorro.statemachine.skills.auth.api.AuthGesture
import com.motorro.statemachine.skills.auth.api.AuthInput
import com.motorro.statemachine.skills.auth.api.AuthResult
import com.motorro.statemachine.skills.auth.api.AuthUiState
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class MainViewModel (api: AuthDataApi) : CommonFlowViewModel<AuthGesture, AuthUiState, AuthInput, AuthResult>(
    api = api,
    init = AuthInput(skippable = true)
)