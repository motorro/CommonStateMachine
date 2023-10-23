/*
 * Copyright 2022 Nikolai Kotchetkov.
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

package com.motorro.statemachine.login.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.statemachine.login.R
import com.motorro.statemachine.login.data.LoginUiState

@Composable
internal fun PasswordError(state: LoginUiState.LoginError, onDismiss: () -> Unit) {

    PasswordEntry(
        state = state.passwordEntry,
        onPasswordChanged = {},
        onPrevious = {},
        onNext = {}
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.login_error_title)) },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(
                    id = R.string.login_error_text,
                    state.passwordEntry.email,
                    state.message
                ),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.login_error_action))
            }
        }
    )
}

@Preview
@Composable
fun PasswordErrorPreview() {
    PasswordError(
        state = LoginUiState.LoginError(
            passwordEntry = LoginUiState.PasswordEntry(
                email = "test@example.com",
                password = "123456",
                actionEnabled = true
            ),
            message = "Login error"
        ),
        onDismiss = {}
    )
}