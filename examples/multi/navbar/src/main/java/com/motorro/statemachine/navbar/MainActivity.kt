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

package com.motorro.statemachine.navbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motorro.statemachine.androidcore.ui.theme.CommonStateMachineTheme
import com.motorro.statemachine.navbar.model.MainViewModel
import com.motorro.statemachine.navbar.model.data.NavbarGesture
import com.motorro.statemachine.timer.ui.TimerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val model: MainViewModel = viewModel()
            val state by model.uiState.collectAsState()

            val currentTimer by remember {
                derivedStateOf {
                    state.timers.first { state.active == it.first }
                }
            }

            CommonStateMachineTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            state.timers.forEachIndexed { i, (key, _) ->
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Star, contentDescription = i.toString()) },
                                    label = { Text("Timer ${ i + 1 }") },
                                    selected = key == state.active,
                                    onClick = { model.update(NavbarGesture.ActiveSelected(key)) }
                                )
                            }
                        }
                    }
                ) { padding ->
                    TimerScreen(
                        modifier = Modifier.padding(padding),
                        title = currentTimer.first.tag,
                        state = currentTimer.second
                    ) {
                        model.update(NavbarGesture.Child(currentTimer.first, it))
                    }
                }
            }
        }
    }
}
