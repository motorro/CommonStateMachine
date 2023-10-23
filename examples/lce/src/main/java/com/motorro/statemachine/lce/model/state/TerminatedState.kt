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

package com.motorro.statemachine.lce.model.state

import com.motorro.statemachine.lce.data.LceUiState
import timber.log.Timber

/**
 * Terminates activity
 */
class TerminatedState : LceLogicalState() {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        Timber.d("Terminated")
        setUiState(LceUiState.Terminated)
    }
}