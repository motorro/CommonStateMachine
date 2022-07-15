package com.motorro.statemachine.registration.view

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.motorro.statemachine.compose.Loading
import com.motorro.statemachine.login.view.LoginScreen
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationGesture.*
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.registration.model.RegistrationViewModel

@Composable
fun RegistrationScreen(onTerminate: @Composable () -> Unit) {
    val model = hiltViewModel<RegistrationViewModel>()
    val state = model.state.collectAsState(RegistrationUiState.Loading)

    BackHandler(onBack = { model.process(Back) })

    when (val uiState = state.value) {
        RegistrationUiState.Loading -> Loading()
        is RegistrationUiState.Welcome -> Welcome(
            state = uiState,
            onTermsToggled = { model.process(TermsAndConditionsToggled) },
            onNext = { model.process(Action) }
        )
        is RegistrationUiState.EmailEntry -> EmailEntry(
            state = uiState,
            onEmailChanged = { model.process(EmailChanged(it)) },
            onNext = { model.process(Action) }
        )
        is RegistrationUiState.Login -> LoginScreen(
            state = uiState.value,
            onGesture = { model.process(Login(it)) }
        )
        is RegistrationUiState.RegistrationPasswordEntry -> TODO()
        RegistrationUiState.Terminated -> { onTerminate() }
    }
}
