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

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.motorro.statemachine.androidcore.compose.Loading
import com.motorro.statemachine.login.view.LoginScreen
import com.motorro.statemachine.register.view.RegistrationScreen
import com.motorro.statemachine.welcome.data.WelcomeGesture.Action
import com.motorro.statemachine.welcome.data.WelcomeGesture.Back
import com.motorro.statemachine.welcome.data.WelcomeGesture.EmailChanged
import com.motorro.statemachine.welcome.data.WelcomeGesture.Login
import com.motorro.statemachine.welcome.data.WelcomeGesture.Register
import com.motorro.statemachine.welcome.data.WelcomeGesture.TermsAndConditionsToggled
import com.motorro.statemachine.welcome.data.WelcomeUiState
import com.motorro.statemachine.welcome.model.WelcomeViewModel

@Composable
fun WelcomeScreen(onTerminate: @Composable () -> Unit) {
    val model = hiltViewModel<WelcomeViewModel>()
    val state = model.state.collectAsState(WelcomeUiState.Loading)

    BackHandler(onBack = { model.process(Back) })

    when (val uiState = state.value) {
        WelcomeUiState.Loading -> Loading()
        is WelcomeUiState.Welcome -> Welcome(
            state = uiState,
            onTermsToggled = { model.process(TermsAndConditionsToggled) },
            onNext = { model.process(Action) }
        )
        is WelcomeUiState.EmailEntry -> EmailEntry(
            state = uiState,
            onEmailChanged = { model.process(EmailChanged(it)) },
            onNext = { model.process(Action) }
        )
        is WelcomeUiState.Login -> LoginScreen(
            state = uiState.value,
            onGesture = { model.process(Login(it)) }
        )
        is WelcomeUiState.Register -> RegistrationScreen(
            state = uiState.value,
            onGesture = { model.process(Register(it))}
        )
        is WelcomeUiState.Complete -> Complete(
            state = uiState,
            onAction = { model.process(Action) }
        )
        WelcomeUiState.Terminated -> { onTerminate() }
    }
}
