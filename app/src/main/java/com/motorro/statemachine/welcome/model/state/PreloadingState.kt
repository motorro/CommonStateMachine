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
import com.motorro.statemachine.welcome.usecase.Preload
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Preloads 'data' to use in [TermsAndConditionsState]
 * Transfers loaded greeting through factory
 */
class PreloadingState(context: WelcomeContext, private val preload: Preload) : WelcomeState(context) {

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(renderer.renderPreloading())
        stateScope.launch {
            val greeting = preload()
            Timber.d("Transferring to welcome screen...")
            setMachineState(factory.welcome(greeting))
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: WelcomeGesture) = when (gesture) {
        is WelcomeGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    /**
     * Exits app
     */
    private fun onBack() {
        Timber.d("Terminating...")
        setMachineState(factory.terminate())
    }

    @ViewModelScoped
    class Factory @Inject constructor(private val preload: Preload) {
        operator fun invoke(context: WelcomeContext) = PreloadingState(context, preload)
    }
}