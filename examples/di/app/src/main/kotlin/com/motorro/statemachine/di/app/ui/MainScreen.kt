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

package com.motorro.statemachine.di.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.motorro.statemachine.androidcore.compose.Loading
import com.motorro.statemachine.di.api.LocalAuth
import com.motorro.statemachine.di.app.R
import com.motorro.statemachine.di.app.data.MainGesture
import com.motorro.statemachine.di.app.data.MainUiState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun MainScreen(state: MainUiState, onGesture: (MainGesture) -> Unit, onTerminated: () -> Unit) {
    val onBack: () -> Unit = remember {
        { onGesture(MainGesture.Back) }
    }

    BackHandler(onBack = onBack)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            val padding = remember {
                Modifier.padding(paddingValues)
            }

            when(state) {
                // Take injected screen implementation
                is MainUiState.Auth -> LocalAuth.current.Screen(
                    state = state.child,
                    onGesture = { onGesture(MainGesture.Auth(it)) },
                    modifier = padding
                )
                is MainUiState.Content -> MainScreenView(
                    state = state,
                    onGesture = onGesture,
                    modifier = padding
                )
                MainUiState.Loading -> Loading(
                    modifier = padding
                )
                MainUiState.Terminated -> LaunchedEffect(Unit) {
                    onTerminated()
                }
            }
        }
    )
}