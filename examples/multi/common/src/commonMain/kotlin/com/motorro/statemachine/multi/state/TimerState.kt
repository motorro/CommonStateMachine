package com.motorro.statemachine.multi.state

import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.commonstatemachine.coroutines.lifecycle.asFlow
import com.motorro.commonstatemachine.lifecycle.MachineLifecycle
import com.motorro.statemachine.commoncore.log.Logger
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
abstract class TimerState(protected val tag: String) : CoroutineState<TimerGesture, TimerUiState>() {
    companion object {
        /**
         * Creates initial state
         */
        fun init(tag: String, lifecycle: MachineLifecycle): TimerState = Running(tag, lifecycle, ZERO)
    }

    protected fun log(message: String) {
        Logger.i("$tag: $message")
    }
}

/**
 * Running state
 */
internal class Running(tag: String, private val lifecycle: MachineLifecycle, private var time: Duration) : TimerState(tag) {
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
            log("Toggled. Stopping...")
            setMachineState(Stopped(tag, lifecycle, time))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startTimer() {
        lifecycle.asFlow(stateScope)
            .onEach {
                log("Machine lifecycle: $it")
            }
            .flatMapLatest { state ->
                when(state) {
                    MachineLifecycle.State.PAUSED -> emptyFlow()
                    MachineLifecycle.State.ACTIVE -> flow {
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
internal class Stopped(tag: String, private val lifecycle: MachineLifecycle, private val time: Duration) : TimerState(tag) {
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
            log("Toggled. Starting...")
            setMachineState(Running(tag, lifecycle, time))
        }
    }
}