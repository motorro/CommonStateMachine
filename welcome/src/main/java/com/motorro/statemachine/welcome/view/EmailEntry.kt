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

package com.motorro.statemachine.welcome.view

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.R
import com.motorro.statemachine.welcome.data.WelcomeUiState

@Composable
fun EmailEntry(state: WelcomeUiState.EmailEntry, onEmailChanged: (String) -> Unit, onNext: () -> Unit) {
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
        state = WelcomeUiState.EmailEntry(
            email = "test@example.com",
            actionEnabled = true
        ),
        onEmailChanged = {},
        onNext = {}
    )
}