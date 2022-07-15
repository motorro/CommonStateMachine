package com.motorro.statemachine.login.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.login.R
import com.motorro.statemachine.login.data.LoginUiState

@Composable
fun PasswordError(state: LoginUiState.LoginError, onDismiss: () -> Unit) {

    PasswordEntry(
        state = state.passwordEntry,
        onPasswordChanged = {},
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