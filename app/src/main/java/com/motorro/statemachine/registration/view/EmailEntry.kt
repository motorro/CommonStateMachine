package com.motorro.statemachine.registration.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.R
import com.motorro.statemachine.registration.data.RegistrationUiState

@Composable
fun EmailEntry(state: RegistrationUiState.EmailEntry, onEmailChanged: (String) -> Unit, onNext: () -> Unit) {
    Column(modifier = Modifier
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
                text = stringResource(id = R.string.email_title),
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(id = R.string.email_desc),
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.email_email_label)) }
            )
        }

        Button(onClick = onNext, enabled = state.actionEnabled) {
            Text(text = stringResource(id = R.string.welcome_next))
        }
    }
}

@Preview
@Composable
fun EmailEntryPreview() {

    EmailEntry(
        state = RegistrationUiState.EmailEntry(
            email = "test@example.com",
            actionEnabled = true
        ),
        onEmailChanged = {},
        onNext = {}
    )
}