package com.motorro.statemachine.welcome.model

import com.motorro.statemachine.commonapi.resources.ResourceWrapper
import com.motorro.statemachine.welcome.data.WelcomeDataState
import com.motorro.statemachine.welcome.data.WelcomeUiState
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

/**
 * Welcome ui-state renderer
 */
@ViewModelScoped
class WelcomeRenderer @Inject constructor(resourceWrapper: ResourceWrapper): ResourceWrapper by resourceWrapper {
    /**
     * Renders preloading
     */
    fun renderPreloading() : WelcomeUiState = WelcomeUiState.Loading

    /**
     * Renders terms
     */
    fun renderTerms(message: String, termsAccepted: Boolean, nextEnabled: Boolean) : WelcomeUiState =
        WelcomeUiState.Welcome(
            message,
            termsAccepted,
            nextEnabled
        )

    /**
     * Renders email entry
     */
    fun renderEmailEntry(data: WelcomeDataState, isValid: Boolean): WelcomeUiState =
        WelcomeUiState.EmailEntry(
            data.email.orEmpty(),
            isValid
        )

    /**
     * Renders email checking
     */
    fun renderChecking(data: WelcomeDataState): WelcomeUiState = WelcomeUiState.Loading

    /**
     * Renders complete screen
     */
    fun renderComplete(email: String): WelcomeUiState = WelcomeUiState.Complete(email)
}