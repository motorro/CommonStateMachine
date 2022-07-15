package com.motorro.statemachine.welcome.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.R
import com.motorro.statemachine.welcome.data.WelcomeUiState

@Composable
fun Welcome(state: WelcomeUiState.Welcome, onTermsToggled: () -> Unit, onNext: () -> Unit) {
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
                text = stringResource(id = R.string.welcome_title),
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = state.message,
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.welcome_accept),
                    style = MaterialTheme.typography.button
                )

                Switch(checked = state.termsAccepted, onCheckedChange = { onTermsToggled() })
            }
        }

        Button(onClick = onNext, enabled = state.actionEnabled) {
            Text(text = stringResource(id = R.string.welcome_next))
        }
    }
}

@Preview
@Composable
fun WelcomePreview() {
    Welcome(
        state = WelcomeUiState.Welcome(
            message = "Welcome to state machine demo",
            termsAccepted = true,
            actionEnabled = true
        ),
        onTermsToggled = {  },
        onNext = { }
    )
}