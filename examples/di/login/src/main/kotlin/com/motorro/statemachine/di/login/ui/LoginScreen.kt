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

package com.motorro.statemachine.di.login.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.androidcore.compose.Error
import com.motorro.statemachine.androidcore.compose.Loading
import com.motorro.statemachine.di.login.data.LoginGesture
import com.motorro.statemachine.di.login.data.LoginUiState

@Composable
internal fun LoginScreen(state: LoginUiState, onGesture: (LoginGesture) -> Unit, modifier: Modifier = Modifier) {
    when(state) {
        is LoginUiState.Form -> LoginFormView(state, onGesture, modifier)
        LoginUiState.Loading -> Loading(modifier)
        is LoginUiState.Error -> Error(
            error = state.error,
            onDismiss = { onGesture(LoginGesture.Action) },
            modifier = modifier
        )
    }
}