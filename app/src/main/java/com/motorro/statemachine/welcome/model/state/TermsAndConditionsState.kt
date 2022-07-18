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

package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.welcome.data.WelcomeGesture
import timber.log.Timber

/**
 * Terms and conditions screen
 * Requires terms to be accepted before going next
 * Shares no common state
 */
class TermsAndConditionsState(
    context: WelcomeContext,
    private val welcomeGreeting: String
) : WelcomeState(context) {

    /**
     * Simple internal state
     */
    private var termsAccepted: Boolean = false

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        render()
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: WelcomeGesture) = when(gesture) {
        WelcomeGesture.TermsAndConditionsToggled -> onTermsToggled()
        WelcomeGesture.Action -> onAction()
        WelcomeGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onTermsToggled() {
        termsAccepted = !termsAccepted
        render()
    }

    private fun onAction() {
        if (termsAccepted) {
            Timber.d("Action. Going to email entry...")
            setMachineState(factory.emailEntry())
        }
    }

    private fun onBack() {
        Timber.d("Back. Terminating...")
        setMachineState(factory.terminate())
    }

    private fun render() {
        setUiState(
            renderer.renderTerms(
                welcomeGreeting,
                termsAccepted,
                termsAccepted
            )
        )
    }
}