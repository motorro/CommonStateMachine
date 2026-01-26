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

package com.motorro.statemachine.di.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import com.motorro.commonstatemachine.flow.CommonFlowDataApi
import com.motorro.statemachine.di.api.data.Session

/**
 * Auth gesture marker
 */
interface AuthGesture

/**
 * Auth UI state marker
 */
interface AuthUiState

/**
 * Auth flow data API
 */
interface AuthDataApi : CommonFlowDataApi<AuthGesture, AuthUiState, Unit, Session.Active, AuthFlowHost>

/**
 * Auth flow ui API
 */
interface AuthUiApi {
    /**
     * Provides composition
     */
    @Composable
    fun AuthenticationScreen(
        state: AuthUiState,
        onGesture: (AuthGesture) -> Unit,
        modifier: Modifier = Modifier
    )
}

/**
 * Local Authentication
 */
val LocalAuth = staticCompositionLocalOf<AuthUiApi> {
    error("No auth ui api provided")
}




