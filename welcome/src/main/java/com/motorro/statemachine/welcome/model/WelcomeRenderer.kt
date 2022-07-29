/*
 * Copyright 2022 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorro.statemachine.welcome.model

import com.motorro.statemachine.commoncore.resources.ResourceWrapper
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