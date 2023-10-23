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

class CompleteState(
    context: WelcomeContext,
    private val email: String
) : WelcomeState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(renderer.renderComplete(email))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: WelcomeGesture) = when(gesture) {
        WelcomeGesture.Back, WelcomeGesture.Action -> onBack()
        else -> super.doProcess(gesture)
    }

    private fun onBack() {
        Timber.d("Back. Terminating...")
        setMachineState(factory.terminate())
    }
}