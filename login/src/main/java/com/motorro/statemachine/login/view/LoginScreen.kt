package com.motorro.statemachine.login.view

import androidx.compose.runtime.Composable
import com.motorro.statemachine.compose.Loading
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState

/**
 * Login screen flow
 */
@Composable
fun LoginScreen(state: LoginUiState, onGesture: (LoginGesture) -> Unit) {
    when (state) {
        LoginUiState.Loading -> Loading()
        is LoginUiState.PasswordEntry -> PasswordEntry(
            state = state,
            onPasswordChanged = { onGesture(LoginGesture.PasswordChanged(it)) },
            onPrevious = { onGesture(LoginGesture.Back) },
            onNext = { onGesture(LoginGesture.Action) }
        )
        is LoginUiState.LoginError -> PasswordError(
            state = state,
            onDismiss = { onGesture(LoginGesture.Action) }
        )
    }
}