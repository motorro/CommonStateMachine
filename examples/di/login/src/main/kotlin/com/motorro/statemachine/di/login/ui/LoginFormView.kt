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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.androidcore.ui.theme.CommonStateMachineTheme
import com.motorro.statemachine.di.login.data.LoginGesture
import com.motorro.statemachine.di.login.data.LoginUiState

@Composable
internal fun LoginFormView(state: LoginUiState.Form, onGesture: (LoginGesture) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.username,
            onValueChange = { onGesture(LoginGesture.UsernameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Username") }
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { onGesture(LoginGesture.PasswordChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = { onGesture(LoginGesture.Action) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = state.loginEnabled
        ) {
            Text(text = "Login")
        }
    }
}

@Preview
@Composable
private fun LoginFormViewPreview() {
    CommonStateMachineTheme {
        LoginFormView(
            state = LoginUiState.Form("user", "password", true),
            onGesture = {}
        )
    }
}
