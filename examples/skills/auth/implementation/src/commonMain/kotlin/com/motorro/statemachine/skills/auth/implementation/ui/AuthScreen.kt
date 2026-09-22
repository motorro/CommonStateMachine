package com.motorro.statemachine.skills.auth.implementation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.motorro.statemachine.auth.appcore.ui.preview.SkillsPreviewComposition
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.AuthUiStateImpl

/**
 * Authentication screen
 * @param state UI state
 * @param onGesture Gesture handler
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AuthScreen(
    state: AuthUiStateImpl,
    modifier: Modifier = Modifier,
    onGesture: (AuthGestureImpl) -> Unit,
) {
    val modifier = modifier.fillMaxSize()
    when(state) {
        AuthUiStateImpl.Loading -> AuthLoadingScreen(modifier)
        is AuthUiStateImpl.Error -> AuthErrorScreen(
            state = state,
            modifier = modifier,
            onGesture = onGesture
        )
        is AuthUiStateImpl.Form -> AuthFormScreen(
            state = state,
            modifier = modifier,
            onGesture = onGesture
        )
    }
}

private class AuthScreenDataProvider : PreviewParameterProvider<AuthUiStateImpl> {
    override val values: Sequence<AuthUiStateImpl> = sequenceOf(
        AuthUiStateImpl.Loading,
        AuthUiStateImpl.Form(
            username = "user",
            password = "password",
            passwordRequirements = "At least 8 characters",
            loginEnabled = true,
            canSkip = true
        ),
        AuthUiStateImpl.Error(
            message = "A recoverable error occurred",
            canRetry = true
        )
    )
}

@Preview
@Composable
private fun SkillsScreenPreview(@PreviewParameter(AuthScreenDataProvider::class) state: AuthUiStateImpl?) {
    if (null == state) return
    SkillsPreviewComposition {
        AuthScreen(
            state = state
        ) {}
    }
}
