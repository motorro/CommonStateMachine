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

package com.motorro.statemachine.navbar.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle
import com.motorro.commonstatemachine.multi.ActiveMachineContainer
import com.motorro.commonstatemachine.multi.GestureProcessor
import com.motorro.commonstatemachine.multi.MachineInit
import com.motorro.commonstatemachine.multi.MachineKey
import com.motorro.commonstatemachine.multi.MultiMachineState
import com.motorro.commonstatemachine.multi.ProxyMachineContainer
import com.motorro.commonstatemachine.multi.UiStateProvider
import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.navbar.model.data.NavbarGesture
import com.motorro.statemachine.navbar.model.data.NavbarUiState
import com.motorro.statemachine.timer.data.TimerGesture
import com.motorro.statemachine.timer.data.TimerKey
import com.motorro.statemachine.timer.data.TimerUiState
import com.motorro.statemachine.timer.state.TimerState
import kotlin.time.Duration

/**
 * Machines are lazily created and paused when not active
 */
internal class NavbarState : MultiMachineState<NavbarGesture, NavbarUiState, TimerGesture, TimerUiState>() {

    private val keys = listOf(
        TimerKey("one"),
        TimerKey("two"),
        TimerKey("three"),
        TimerKey("four"),
    )

    /**
     * Machines are lazily created and paused when not active
     */
    override val container: ActiveMachineContainer<TimerGesture, TimerUiState> = ProxyMachineContainer.some(
        keys.map { key ->
            object : MachineInit<TimerGesture, TimerUiState> {
                override val key: TimerKey = key
                override val initialUiState: TimerUiState = TimerUiState.Stopped(Duration.ZERO)
                override val init: (MachineLifecycle) -> CommonMachineState<TimerGesture, TimerUiState> = {
                    val tag = requireNotNull(key.tag)
                    Logger.i("Creating machine for $tag")
                    TimerState.init(tag, it)
                }
            }
        }
    )

    /**
     * Updates child machines with gestures if relevant
     * @param parent Parent gesture
     * @param processor Use it to send child gesture to the relevant child machine
     */
    override fun mapGesture(parent: NavbarGesture, processor: GestureProcessor<TimerGesture, TimerUiState>) = when(parent) {
        is NavbarGesture.ActiveSelected -> {
            Logger.i("Activating: ${parent.key}")
            container.setActive(parent.key)
            updateUi()
        }
        NavbarGesture.Disposed -> {
            Logger.i("Disposing inactive...")
            container.disposeInactive()
        }
        is NavbarGesture.Child -> {
            Logger.i("Gesture ${parent.gesture} for key: ${parent.key}")
            processor.process(parent.key, parent.gesture)
        }
    }

    /**
     * Maps combined child UI state to parent
     * @param provider Provides child UI states
     * @param changedKey Key of machine that changed the UI state. Null if called explicitly via [updateUi]
     * @see updateUi
     */
    override fun mapUiState(provider: UiStateProvider<TimerUiState>, changedKey: MachineKey<*, out TimerUiState>?): NavbarUiState {
        return NavbarUiState(
            keys.map { key ->
                key to provider.getValue(key)
            },
            container.getActive().first()
        )
    }
}