package com.motorro.statemachine.multi.state

import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.commonstatemachine.coroutines.lifecycle.asFlow
import com.motorro.commonstatemachine.lifecycle.LifecycleState
import com.motorro.statemachine.multi.data.TimerGesture
import com.motorro.statemachine.multi.data.TimerUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds

/**
 * Timer state
 */
abstract class TimerState : CoroutineState<TimerGesture, TimerUiState>() {
    companion object {
        /**
         * Creates initial state
         */
        fun init(lifecycle: LifecycleState): TimerState = Running(lifecycle, ZERO)
    }
}

/**
 * Running state
 */
internal class Running(private val lifecycle: LifecycleState, private var time: Duration) : TimerState() {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        render()
        startTimer()
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: TimerGesture) {
        if (gesture is TimerGesture.Toggle) {
            setMachineState(Stopped(lifecycle, time))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startTimer() {
        lifecycle.asFlow(stateScope)
            .flatMapLatest { state ->
                when(state) {
                    LifecycleState.State.PAUSED -> emptyFlow()
                    LifecycleState.State.ACTIVE -> flow {
                        while (currentCoroutineContext().isActive) {
                            delay(DELAY)
                            emit(Unit)
                        }
                    }
                }
            }
            .onEach {
                time += 1.seconds
                render()
            }
            .launchIn(stateScope)
    }

    /**
     * Updates UI
     */
    private fun render() {
        setUiState(TimerUiState.Running(time))
    }

    internal companion object {
        val DELAY = 1.seconds
    }
}

/**
 * Stopped time
 */
internal class Stopped(private val lifecycle: LifecycleState, private val time: Duration) : TimerState() {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(TimerUiState.Stopped(time))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: TimerGesture) {
        if (gesture is TimerGesture.Toggle) {
            setMachineState(Running(lifecycle, time))
        }
    }
}