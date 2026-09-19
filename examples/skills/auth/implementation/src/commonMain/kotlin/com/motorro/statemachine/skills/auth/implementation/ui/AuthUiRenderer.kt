package com.motorro.statemachine.skills.auth.implementation.ui

import com.motorro.commonstatemachine.skills.domain.exception.AppException
import com.motorro.statemachine.skills.auth.implementation.data.AuthStateData
import com.motorro.statemachine.skills.auth.implementation.data.AuthUiStateImpl

/**
 * Transforms the data state to the UI state
 */
internal interface AuthUiRenderer {
    /**
     * Renders loading.
     * The result would be an object so we don't need interstate data here.
     */
    fun renderLoading(): AuthUiStateImpl

    /**
     * Renders login form.
     * The data could be fully taken from the data state.
     * @param data Data-state to transform
     * @param isValid If the form is valid or not
     */
    fun renderForm(data: AuthStateData, isValid: Boolean): AuthUiStateImpl

    /**
     * Renders error that takes the full screen
     * - doesn't require any data-state
     * - requires an error and takes a message from it
     * @param error Error to present to the user
     * @param canRetry Error could be retried
     */
    fun renderFullScreenError(error: AppException, canRetry: Boolean): AuthUiStateImpl
}