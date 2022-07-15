package com.motorro.statemachine.login.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.login.R

@Composable
fun PasswordEntry(state: LoginUiState.PasswordEntry, onPasswordChanged: (String) -> Unit, onNext: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier
        .verticalScroll(scrollState)
        .fillMaxSize()
        .statusBarsPadding()
        .imePadding()
        .navigationBarsPadding()
        .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.login_password_entry_title),
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.email,
                style = MaterialTheme.typography.h3
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.login_password_entry_desc),
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.login_password_entry_label)) },
                visualTransformation = PasswordVisualTransformation()
            )
        }

        Button(onClick = onNext, enabled = state.actionEnabled) {
            Text(text = stringResource(id = R.string.login_password_entry_next))
        }
    }
}

@Preview
@Composable
fun EmailEntryPreview() {
    PasswordEntry(
        state = LoginUiState.PasswordEntry(
            email = "test@example.com",
            password = "123456",
            actionEnabled = true
        ),
        onPasswordChanged = {},
        onNext = {}
    )
}