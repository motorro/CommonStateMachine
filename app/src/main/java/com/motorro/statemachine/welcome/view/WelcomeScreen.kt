package com.motorro.statemachine.welcome.view

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.motorro.statemachine.androidcore.compose.Loading
import com.motorro.statemachine.login.view.LoginScreen
import com.motorro.statemachine.register.view.RegistrationScreen
import com.motorro.statemachine.welcome.data.WelcomeGesture.*
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
