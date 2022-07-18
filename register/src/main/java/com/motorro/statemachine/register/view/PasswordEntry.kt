package com.motorro.statemachine.register.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.register.R
import com.motorro.statemachine.register.data.RegisterUiState

@Composable
fun PasswordEntry(
    state: RegisterUiState.PasswordEntry,
    onPasswordChanged: (String) -> Unit,
    onRepeatPasswordChanged: (String) -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
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
                text = stringResource(id = R.string.register_password_entry_title),
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = state.email,
                style = MaterialTheme.typography.h3
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.register_password_entry_desc),
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.register_password_entry_label)) },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.repeatPassword,
                onValueChange = onRepeatPasswordChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.register_repeat_password_entry_label)) },
                visualTransformation = PasswordVisualTransformation()
            )

            val validationError = state.validationError
            if (null != validationError) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    color = MaterialTheme.colors.error,
                    text = validationError
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = onPrevious) {
                Text(text = stringResource(id = R.string.register_password_entry_prev))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = onNext) {
                Text(text = stringResource(id = R.string.register_password_entry_next))
            }
        }
    }
}

@Preview
@Composable
fun EmailEntryPreview() {
    PasswordEntry(
        state = RegisterUiState.PasswordEntry(
            email = "test@example.com",
            password = "123456",
            repeatPassword = "123456",
            validationError = "Too complex password"
        ),
        onPasswordChanged = {},
        onRepeatPasswordChanged = {},
        onPrevious = {},
        onNext = {}
    )
}