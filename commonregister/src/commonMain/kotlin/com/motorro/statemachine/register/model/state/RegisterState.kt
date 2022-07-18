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

package com.motorro.statemachine.register.model.state

import com.motorro.commonstatemachine.CoroutineState
import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.register.data.RegisterGesture
import com.motorro.statemachine.register.data.RegisterUiState

/**
 * Base class for registration flow state
 */
abstract class RegisterState(
    context: RegisterContext
): CoroutineState<RegisterGesture, RegisterUiState>(), RegisterContext by context {
    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: RegisterGesture) {
        Logger.w("Unsupported gesture: %s", gesture)
    }
}