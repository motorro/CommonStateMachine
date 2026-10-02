package com.motorro.commonstatemachine.examples.skills.auth.implementation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.motorro.commonstatemachine.examples.auth.appcore.ui.preview.SkillsPreviewComposition
import com.motorro.commonstatemachine.examples.auth.appcore.ui.theme.Dimensions
import com.motorro.commonstatemachine.examples.skills.auth.implementation.Res
import com.motorro.commonstatemachine.examples.skills.auth.implementation.btn_login
import com.motorro.commonstatemachine.examples.skills.auth.implementation.btn_skip
import com.motorro.commonstatemachine.examples.skills.auth.implementation.data.AuthGestureImpl
import com.motorro.commonstatemachine.examples.skills.auth.implementation.data.AuthUiStateImpl
import com.motorro.commonstatemachine.examples.skills.auth.implementation.input_password
import com.motorro.commonstatemachine.examples.skills.auth.implementation.input_username
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AuthFormScreen(
    state: AuthUiStateImpl.Form,
    modifier: Modifier = Modifier,
    onGesture: (AuthGestureImpl) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(Dimensions.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = state.username,
            onValueChange = { onGesture(AuthGestureImpl.Form.UsernameChanged(it)) },
            label = { Text(stringResource(Res.string.input_username)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(Dimensions.medium))
        TextField(
            value = state.password,
            onValueChange = { onGesture(AuthGestureImpl.Form.PasswordChanged(it)) },
            label = { Text(stringResource(Res.string.input_password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )
        Text(
            text = state.passwordRequirements,
            modifier = Modifier.fillMaxWidth().padding(top = Dimensions.small),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(Dimensions.medium))
        Button(
            onClick = { onGesture(AuthGestureImpl.Action) },
            enabled = state.loginEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.btn_login))
        }
        if (state.canSkip) {
            Spacer(Modifier.height(Dimensions.medium))
            OutlinedButton(
                onClick = { onGesture(AuthGestureImpl.Form.Skip) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.btn_skip))
            }
        }
    }
}

private class AuthFormDataProvider : PreviewParameterProvider<AuthUiStateImpl.Form> {
    override val values: Sequence<AuthUiStateImpl.Form> = sequenceOf(
        AuthUiStateImpl.Form(
            username = "",
            password = "",
            passwordRequirements = "At least 8 characters",
            loginEnabled = false,
            canSkip = true
        ),
        AuthUiStateImpl.Form(
            username = "user",
            password = "password",
            passwordRequirements = "At least 8 characters",
            loginEnabled = true,
            canSkip = true
        ),
        AuthUiStateImpl.Form(
            username = "user",
            password = "password",
            passwordRequirements = "At least 8 characters",
            loginEnabled = true,
            canSkip = false
        )
    )
}

@Preview
@Composable
private fun AuthFormPreview(@PreviewParameter(AuthFormDataProvider::class) state: AuthUiStateImpl.Form?) {
    if (null == state) return
    SkillsPreviewComposition {
        AuthFormScreen(
            state = state,
            modifier = Modifier.fillMaxSize()
        ) {}
    }
}
