package com.motorro.statemachine.welcome.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
fun Complete(state: WelcomeUiState.Complete, onAction: () -> Unit) {
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
                text = stringResource(id = R.string.complete_title),
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = state.email,
                style = MaterialTheme.typography.h5
            )
        }

        Button(onClick = onAction) {
            Text(text = stringResource(id = R.string.complete_action))
        }
    }
}

@Preview
@Composable
fun CompletePreview() {
    Complete(
        state = WelcomeUiState.Complete("test@example.com"),
        onAction = {}
    )
}