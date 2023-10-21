package com.motorro.statemachine.parallel.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle
import com.motorro.commonstatemachine.multi.GestureProcessor
import com.motorro.commonstatemachine.multi.MachineInit
import com.motorro.commonstatemachine.multi.MultiMachineState
import com.motorro.commonstatemachine.multi.ProxyMachineContainer
import com.motorro.commonstatemachine.multi.UiStateProvider
import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.multi.data.TimerGesture
import com.motorro.statemachine.multi.data.TimerKey
import com.motorro.statemachine.multi.data.TimerUiState
import com.motorro.statemachine.multi.state.TimerState
import com.motorro.statemachine.parallel.model.data.ParallelGesture
import com.motorro.statemachine.parallel.model.data.ParallelUiState
import kotlin.time.Duration

/**
 * Machines run in parallel. All machines are active
 */
internal class ParallelState : MultiMachineState<ParallelGesture, ParallelUiState>() {
    private val topKey = TimerKey("top")
    private val bottomKey = TimerKey("bottom")

    /**
     * Machines run in parallel and always active
     */
    override val container = ProxyMachineContainer.allTogether(
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
    override fun mapGesture(parent: ParallelGesture, processor: GestureProcessor) = when(parent) {
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
     */
    override fun mapUiState(provider: UiStateProvider): ParallelUiState = ParallelUiState(
        top = provider.getValue(topKey),
        bottom = provider.getValue(bottomKey)
    )
}