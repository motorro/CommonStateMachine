package com.motorro.statemachine.skills.auth.implementation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.skills.auth.api.AuthGesture
import com.motorro.statemachine.skills.auth.api.AuthUiApi
import com.motorro.statemachine.skills.auth.api.AuthUiState
import com.motorro.statemachine.skills.auth.implementation.data.AuthUiStateImpl
import com.motorro.statemachine.skills.auth.implementation.ui.AuthScreen
import org.koin.core.annotation.Factory

/**
 * UI API implementation
 */
@Factory
internal class AuthUiApiImpl : AuthUiApi {
    @Composable
    override fun Screen(
        state: AuthUiState,
        onGesture: (AuthGesture) -> Unit,
        modifier: Modifier
    ) = AuthScreen(
        state = state as AuthUiStateImpl,
        onGesture = onGesture
    )
}