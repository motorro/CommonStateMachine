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

package com.motorro.statemachine.skills.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.motorro.commonstatemachine.flow.viewmodel.CommonFlowComposition
import com.motorro.statemachine.auth.appcore.ui.design.SkillsAppBar
import com.motorro.statemachine.auth.appcore.ui.design.SkillsLoading
import com.motorro.statemachine.skills.app.MainViewModel
import com.motorro.statemachine.skills.app.R
import com.motorro.statemachine.skills.app.data.MainGesture
import com.motorro.statemachine.skills.app.data.MainUiState
import com.motorro.statemachine.skills.auth.api.AuthGesture
import com.motorro.statemachine.skills.auth.api.AuthUiApi
import io.github.aakira.napier.Napier
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MainScreen(onTerminated: () -> Unit) {
    val viewModel: MainViewModel = koinViewModel()

    CommonFlowComposition(
        viewModel = viewModel,
        navigationBackHandler = { enabled, onBack ->
            BackHandler(enabled, onBack)
        },
        finish = {
            Napier.i { "Finished with: $it" }
            onTerminated()
        },
        content = { state, onGesture ->
            when(state) {
                is MainUiState.Splash -> SplashScreen()
                is MainUiState.Auth -> {
                    val onAuthGesture = remember {
                        { child: AuthGesture -> onGesture(MainGesture.Auth(child)) }
                    }
                    AuthScreen(state = state, onGesture = onAuthGesture)
                }
                MainUiState.Friends -> ContentScreen()
            }
        }
    )
}

@Composable
private fun AuthScreen(
    state: MainUiState.Auth,
    onGesture: (AuthGesture) -> Unit
) {
    val uiApi: AuthUiApi = koinInject()
    Scaffold(
        topBar = {
            SkillsAppBar(
                title = stringResource(R.string.app_name),
                topLevel = true
            )
        },
        content = { paddingValues ->
            uiApi.Screen(
                state = state.child,
                onGesture = onGesture,
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SplashScreen() {
    Scaffold(
        topBar = {
            SkillsAppBar(
                title = stringResource(R.string.app_name),
                topLevel = true
            )
        },
        content = { paddingValues ->
            SkillsLoading(Modifier.padding(paddingValues))
        }
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ContentScreen() {
    Scaffold(
        topBar = {
            SkillsAppBar(
                title = stringResource(R.string.app_name),
                topLevel = true
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Friends placeholder"
                )
            }
        }
    )
}
