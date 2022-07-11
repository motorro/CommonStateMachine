package com.motorro.statemachine.registration.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.registration.model.RegistrationViewModel

@Composable
fun RegistrationScreen() {
    val model: RegistrationViewModel = hiltViewModel()
    val state by model.state.observeAsState(RegistrationUiState.Loading)

    when (state) {
        RegistrationUiState.Loading -> Loading()
        is RegistrationUiState.Welcome -> TODO()
        is RegistrationUiState.EmailEntry -> TODO()
        is RegistrationUiState.LoginPasswordEntry -> TODO()
        is RegistrationUiState.RegistrationPasswordEntry -> TODO()
    }
}
