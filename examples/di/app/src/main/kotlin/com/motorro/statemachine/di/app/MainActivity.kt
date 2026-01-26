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

package com.motorro.statemachine.di.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.motorro.statemachine.androidcore.ui.theme.CommonStateMachineTheme
import com.motorro.statemachine.di.api.AuthUiApi
import com.motorro.statemachine.di.api.LocalAuth
import com.motorro.statemachine.di.app.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Provided by variant module
     */
    @field:Inject
    lateinit var authUiApi: AuthUiApi

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state by viewModel.uiState.collectAsStateWithLifecycle()

            CommonStateMachineTheme {
                WithLocals {
                    MainScreen(
                        state = state,
                        onGesture = viewModel::process,
                        onTerminated = ::finish
                    )
                }
            }
        }
    }

    /**
     * Provides injected locals
     */
    @Composable
    internal fun WithLocals(block: @Composable () -> Unit) {
        CompositionLocalProvider(LocalAuth provides authUiApi) {
            block()
        }
    }
}
