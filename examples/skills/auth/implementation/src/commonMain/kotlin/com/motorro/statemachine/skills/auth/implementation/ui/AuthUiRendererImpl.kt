package com.motorro.statemachine.skills.auth.implementation.ui

import com.motorro.commonstatemachine.skills.domain.exception.AppException
import com.motorro.statemachine.skills.auth.implementation.data.AuthStateData
import com.motorro.statemachine.skills.auth.implementation.data.AuthUiStateImpl
import org.koin.core.annotation.Factory

/**
 * UI renderer implementation
 */
@Factory
internal class AuthUiRendererImpl : AuthUiRenderer {

    override fun renderLoading(): AuthUiStateImpl = AuthUiStateImpl.Loading

    override fun renderForm(data: AuthStateData, isValid: Boolean) = AuthUiStateImpl.Form(
        username = data.username,
        password = data.password,
        passwordRequirements = data.passwordRequirements.description,
        loginEnabled = isValid,
        canSkip = data.input.skippable
    )

    override fun renderFullScreenError(error: AppException, canRetry: Boolean) = AuthUiStateImpl.Error(
        message = error.message,
        canRetry = canRetry
    )
}