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

package com.motorro.statemachine.lce.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.motorro.statemachine.lce.R
import com.motorro.statemachine.lce.data.LceUiState

@Composable
fun LoadError(state: LceUiState.Error, onRetry: () -> Unit, onBack: () -> Unit, onExit: () -> Unit) {
    AlertDialog(
        onDismissRequest = onBack,
        title = { Text(stringResource(R.string.error_title)) },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.error.message.orEmpty(),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.error_retry))
            }
        },
        dismissButton = {
            Button(onClick = onExit) {
                Text(text = stringResource(R.string.error_exit))
            }
        }
    )
}