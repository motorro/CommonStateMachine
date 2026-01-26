/*
 * Copyright 2026 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorro.statemachine.di.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.di.api.AuthDataApi
import com.motorro.statemachine.di.api.AuthFlowHost
import com.motorro.statemachine.di.api.AuthGesture
import com.motorro.statemachine.di.api.AuthUiApi
import com.motorro.statemachine.di.api.AuthUiState
import com.motorro.statemachine.di.login.data.LoginDataState
import com.motorro.statemachine.di.login.data.LoginGesture
import com.motorro.statemachine.di.login.data.LoginUiState
import com.motorro.statemachine.di.login.state.LoginFactory
import com.motorro.statemachine.di.login.ui.LoginScreen
import jakarta.inject.Inject

/**
 * Auth data API implementation for login flow
 */
internal class LoginDataApi @Inject constructor(private val createFactory: LoginFactory.Factory) : AuthDataApi {
    /**
     * Initializes flow
     */
    override fun init(flowHost: AuthFlowHost, input: Unit?) = createFactory(flowHost).form(LoginDataState())

    /**
     * Returns default UI state
     */
    override fun getDefaultUiState() = LoginUiState.Loading

    /**
     * Returns back gesture for this flow
     */
    override fun getBackGesture(): AuthGesture = LoginGesture.Back
}

/**
 * Auth UI API implementation for login flow
 */
internal class LoginUiApi @Inject constructor() : AuthUiApi {
    @Composable
    override fun AuthenticationScreen(
        state: AuthUiState,
        onGesture: (AuthGesture) -> Unit,
        modifier: Modifier
    ) = LoginScreen(
        state = state as LoginUiState,
        onGesture = onGesture,
        modifier = modifier
    )
}