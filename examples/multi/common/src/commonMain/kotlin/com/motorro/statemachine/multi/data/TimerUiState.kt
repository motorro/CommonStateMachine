/*
 * Copyright 2023 Nikolai Kotchetkov.
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

package com.motorro.statemachine.multi.data

import kotlin.time.Duration

/**
 * Timer UI state
 */
sealed class TimerUiState {
    /**
     * Timer count
     */
    abstract val time: Duration

    /**
     * Running
     */
    data class Running(override val time: Duration) : TimerUiState()

    /**
     * Stopped
     */
    data class Stopped(override val time: Duration) : TimerUiState()
}