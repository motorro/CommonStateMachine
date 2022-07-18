package com.motorro.statemachine.register.view

import androidx.compose.runtime.Composable
import com.motorro.statemachine.androidcore.compose.Loading
import com.motorro.statemachine.register.data.RegisterGesture
import com.motorro.statemachine.register.data.RegisterUiState

/**
 * Login screen flow
 */
@Composable
fun RegistrationScreen(state: RegisterUiState, onGesture: (RegisterGesture) -> Unit) {
    when (state) {
        RegisterUiState.Loading -> Loading()
        is RegisterUiState.PasswordEntry -> PasswordEntry(
            state = state,
            onPasswordChanged = { onGesture(RegisterGesture.PasswordChanged(it)) },
            onRepeatPasswordChanged = { onGesture(RegisterGesture.RepeatPasswordChanged(it)) },
            onPrevious = { onGesture(RegisterGesture.Back) },
            onNext = { onGesture(RegisterGesture.Action) }
        )
    }
}