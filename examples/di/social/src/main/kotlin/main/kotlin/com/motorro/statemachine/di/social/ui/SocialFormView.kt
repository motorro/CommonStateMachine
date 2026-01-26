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

package main.kotlin.com.motorro.statemachine.di.social.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.statemachine.androidcore.ui.theme.CommonStateMachineTheme
import main.kotlin.com.motorro.statemachine.di.social.data.SocialGesture
import main.kotlin.com.motorro.statemachine.di.social.data.SocialUiState

@Composable
internal fun SocialFormView(state: SocialUiState.Form, onGesture: (SocialGesture) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = modifier.fillMaxWidth().clickable { onGesture(SocialGesture.Action) } ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.AccountCircle,
                    contentDescription = "Login with Social",
                    modifier = Modifier.size(96.dp)
                )
                Text(
                    text = "Login with Social",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun SocialFormViewPreview() {
    CommonStateMachineTheme {
        SocialFormView(
            state = SocialUiState.Form,
            onGesture = {}
        )
    }
}
