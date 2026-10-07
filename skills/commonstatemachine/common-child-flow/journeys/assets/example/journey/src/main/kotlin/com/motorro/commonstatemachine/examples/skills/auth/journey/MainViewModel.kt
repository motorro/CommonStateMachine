package com.motorro.commonstatemachine.examples.skills.auth.journey

import com.motorro.commonstatemachine.examples.skills.auth.api.AuthDataApi
import com.motorro.commonstatemachine.examples.skills.auth.api.AuthGesture
import com.motorro.commonstatemachine.examples.skills.auth.api.AuthInput
import com.motorro.commonstatemachine.examples.skills.auth.api.AuthResult
import com.motorro.commonstatemachine.examples.skills.auth.api.AuthUiState
import com.motorro.commonstatemachine.flow.viewmodel.CommonFlowViewModel
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class MainViewModel (api: AuthDataApi) : CommonFlowViewModel<AuthGesture, AuthUiState, AuthInput, AuthResult>(
    api = api,
    init = AuthInput(skippable = true)
)