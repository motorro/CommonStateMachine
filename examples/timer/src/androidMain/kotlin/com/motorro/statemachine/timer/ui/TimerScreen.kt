/*
 * Copyright 2023 Nikolai Kotchetkov.
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

package com.motorro.statemachine.timer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.motorro.statemachine.timer.data.TimerGesture
import com.motorro.statemachine.timer.data.TimerUiState
import kotlin.time.Duration

@Composable
fun TimerScreen(modifier: Modifier = Modifier, title: String?, state: TimerUiState, onGesture: (TimerGesture) -> Unit) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {
            Text(style = MaterialTheme.typography.headlineLarge, text = title.orEmpty())
            Text(style = MaterialTheme.typography.displayLarge, text = state.time.format())
            Button(onClick = { onGesture(TimerGesture.Toggle) }) {
                Text(
                    style = MaterialTheme.typography.labelMedium,
                    text = when(state) {
                        is TimerUiState.Running -> "Stop"
                        is TimerUiState.Stopped -> "Start"
                    }
                )
            }
        }
    }
}

private fun Duration.format(): String {
    fun Number.pad(): String = toString().padStart(2, '0')
    return toComponents{ h, m,s, _ ->
        "${h.pad()}:${m.pad()}:${s.pad()}"
    }
}