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

package com.motorro.statemachine.parallel.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle
import com.motorro.commonstatemachine.multi.GestureProcessor
import com.motorro.commonstatemachine.multi.MachineInit
import com.motorro.commonstatemachine.multi.MachineKey
import com.motorro.commonstatemachine.multi.MultiMachineState
import com.motorro.commonstatemachine.multi.ProxyMachineContainer
import com.motorro.commonstatemachine.multi.UiStateProvider
import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.parallel.model.data.ParallelGesture
import com.motorro.statemachine.parallel.model.data.ParallelUiState
import com.motorro.statemachine.timer.data.TimerGesture
import com.motorro.statemachine.timer.data.TimerKey
import com.motorro.statemachine.timer.data.TimerUiState
import com.motorro.statemachine.timer.state.TimerState
import kotlin.time.Duration

/**
 * Machines run in parallel. All machines are active
 */
internal class ParallelState : MultiMachineState<ParallelGesture, ParallelUiState, TimerGesture, TimerUiState>() {
    private val topKey = TimerKey("top")
    private val bottomKey = TimerKey("bottom")

    /**
     * Machines run in parallel and always active
     */
    override val container: ProxyMachineContainer<TimerGesture, TimerUiState> = ProxyMachineContainer.allTogether(
        listOf(
            object : MachineInit<TimerGesture, TimerUiState> {
                override val key: TimerKey = topKey
                override val initialUiState: TimerUiState = TimerUiState.Stopped(Duration.ZERO)
                override val init: (MachineLifecycle) -> CommonMachineState<TimerGesture, TimerUiState> = {
                    val tag = requireNotNull(key.tag)
                    Logger.i("Creating machine for $tag")
                    TimerState.init(tag, it)
                }
            },
            object : MachineInit<TimerGesture, TimerUiState> {
                override val key: TimerKey = bottomKey
                override val initialUiState: TimerUiState = TimerUiState.Stopped(Duration.ZERO)
                override val init: (MachineLifecycle) -> CommonMachineState<TimerGesture, TimerUiState> = {
                    val tag = requireNotNull(key.tag)
                    Logger.i("Creating machine for $tag")
                    TimerState.init(tag, it)
                }
            }
        )
    )

    /**
     * Updates child machines with gestures if relevant
     * @param parent Parent gesture
     * @param processor Use it to send child gesture to the relevant child machine
     */
    override fun mapGesture(parent: ParallelGesture, processor: GestureProcessor<TimerGesture, TimerUiState>) = when(parent) {
        is ParallelGesture.Top -> {
            Logger.i("Top gesture: $parent")
            processor.process(topKey, parent.gesture)
        }
        is ParallelGesture.Bottom -> {
            Logger.i("Bottom gesture: $parent")
            processor.process(bottomKey, parent.gesture)
        }
    }

    /**
     * Maps combined child UI state to parent
     * @param provider Provides child UI states
     * @param changedKey Key of machine that changed the UI state. Null if called explicitly via [updateUi]
     * @see updateUi
     */
    override fun mapUiState(
        provider: UiStateProvider<TimerUiState>,
        changedKey: MachineKey<*, out TimerUiState>?
    ): ParallelUiState = ParallelUiState(
        top = provider.getValue(topKey),
        bottom = provider.getValue(bottomKey)
    )
}