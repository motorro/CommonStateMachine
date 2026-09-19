package com.motorro.statemachine.skills.auth.implementation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.motorro.statemachine.auth.appcore.ui.preview.SkillsPreviewComposition
import com.motorro.statemachine.auth.appcore.ui.theme.Dimensions
import com.motorro.statemachine.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.statemachine.skills.auth.implementation.data.AuthUiStateImpl

@Composable
internal fun AuthErrorScreen(
    state: AuthUiStateImpl.Error,
    modifier: Modifier = Modifier,
    onGesture: (AuthGestureImpl) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize().padding(Dimensions.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = state.message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(Dimensions.medium))
        Button(onClick = { onGesture(AuthGestureImpl.Action) }) {
            Text(if (state.canRetry) "Retry" else "Close")
        }
    }
}

private class AuthErrorDataProvider : PreviewParameterProvider<AuthUiStateImpl.Error> {
    override val values: Sequence<AuthUiStateImpl.Error> = sequenceOf(
        AuthUiStateImpl.Error(
            message = "A recoverable error occurred",
            canRetry = true
        ),
        AuthUiStateImpl.Error(
            message = "A non-recoverable error occurred",
            canRetry = false
        )
    )
}

@Preview
@Composable
private fun AuthErrorPreview(@PreviewParameter(AuthErrorDataProvider::class) state: AuthUiStateImpl.Error?) {
    if (null == state) return
    SkillsPreviewComposition {
        AuthErrorScreen(
            state = state,
            modifier = Modifier.fillMaxSize()
        ) {}
    }
}
