package com.motorro.commonstatemachine.examples.skills.auth.implementation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.commonstatemachine.examples.skills.auth.api.AuthGesture
import com.motorro.commonstatemachine.examples.skills.auth.api.AuthUiApi
import com.motorro.commonstatemachine.examples.skills.auth.api.AuthUiState
import com.motorro.commonstatemachine.examples.skills.auth.implementation.data.AuthUiStateImpl
import com.motorro.commonstatemachine.examples.skills.auth.implementation.ui.AuthScreen
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
        modifier = modifier,
        onGesture = onGesture
    )
}