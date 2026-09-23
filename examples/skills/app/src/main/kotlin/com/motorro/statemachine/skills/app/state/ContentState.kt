/*
 * Copyright 2026 Nikolai Kotchetkov.
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

package com.motorro.statemachine.skills.app.state

import com.motorro.statemachine.skills.app.data.MainGesture
import io.github.aakira.napier.Napier
import org.koin.core.annotation.Factory

/**
 * Content stub
 */
internal class ContentState(context: MainContext) : BaseMainState(context) {
    override fun doStart() {
        setUiState(renderer.renderContent())
    }

    override fun doProcess(gesture: MainGesture) {
        when (gesture) {
            MainGesture.Back -> {
                Napier.d("Back. Terminating...")
                setMachineState(factory.terminated())
            }
            else -> super.doProcess(gesture)
        }
    }

    @Factory
    class StateFactory() {
        fun create(context: MainContext) = ContentState(
            context
        )
    }
}