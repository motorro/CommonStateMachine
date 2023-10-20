package com.motorro.statemachine.navbar.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle
import com.motorro.commonstatemachine.multi.ActiveMachineContainer
import com.motorro.commonstatemachine.multi.GestureProcessor
import com.motorro.commonstatemachine.multi.MachineInit
import com.motorro.commonstatemachine.multi.MultiMachineState
import com.motorro.commonstatemachine.multi.UiStateProvider
import com.motorro.statemachine.multi.data.TimerGesture
import com.motorro.statemachine.multi.data.TimerKey
import com.motorro.statemachine.multi.data.TimerUiState
import com.motorro.statemachine.multi.state.TimerState
import com.motorro.statemachine.navbar.model.data.NavbarGesture
import com.motorro.statemachine.navbar.model.data.NavbarUiState
import kotlin.time.Duration

internal class NavbarState : MultiMachineState<NavbarGesture, NavbarUiState>() {

    private val keys = listOf(
        TimerKey("one"),
        TimerKey("two"),
        TimerKey("three"),
        TimerKey("four"),
    )

    override val container = ActiveMachineContainer.some(
        keys.map { key ->
            object : MachineInit<TimerGesture, TimerUiState> {
                override val key: TimerKey = key
                override val initialUiState: TimerUiState = TimerUiState.Stopped(Duration.ZERO)
                override val init: (MachineLifecycle) -> CommonMachineState<TimerGesture, TimerUiState> = {
                    TimerState.init(it)
                }
            }
        }
    )

    /**
     * Updates child machines with gestures if relevant
     * @param parent Parent gesture
     * @param processor Use it to send child gesture to the relevant child machine
     */
    override fun mapGesture(parent: NavbarGesture, processor: GestureProcessor) = when(parent) {
        is NavbarGesture.ActiveSelected -> {
            container.setActive(parent.key)
            updateUi()
        }
        is NavbarGesture.Child -> processor.process(parent.key, parent.gesture)
    }

    /**
     * Maps combined child UI state to parent
     * @param provider Provides child UI states
     */
    override fun mapUiState(provider: UiStateProvider): NavbarUiState {
        return NavbarUiState(
            keys.map { key ->
                key to provider.getValue(key)
            },
            container.getActive().first()
        )
    }
}