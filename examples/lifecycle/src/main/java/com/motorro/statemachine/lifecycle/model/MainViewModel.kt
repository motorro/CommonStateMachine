package com.motorro.statemachine.lifecycle.model

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.commonstatemachine.lifecycle.UiMachineLifecycle
import com.motorro.commonstatemachine.lifecycle.UiMachineLifecycle.Companion.bindLifecycle
import com.motorro.statemachine.timer.data.TimerGesture
import com.motorro.statemachine.timer.data.TimerUiState
import com.motorro.statemachine.timer.state.TimerState
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

class MainViewModel : ViewModel() {

    /**
     * View lifecycle to pause/resume pending operations
     * like data subscriptions, GPS tracking etc
     * You may convert it to `StateFlow` later
     * @see com.motorro.commonstatemachine.coroutines.lifecycle.LifecycleStateFlowKt.asFlow
     */
    private val ls = UiMachineLifecycle()

    private val machine = FlowStateMachine(TimerUiState.Stopped(Duration.ZERO)) {
        TimerState.init("Timer", ls)
    }

    /**
     * Call to bind view lifecycle
     */
    fun bindLifecycle(lifecycle: Lifecycle): UiMachineLifecycle.UiObserver {
        return ls.bindLifecycle(lifecycle)
    }

    val uiState: StateFlow<TimerUiState> get() = machine.uiState

    fun update(gesture: TimerGesture) = machine.process(gesture)

    override fun onCleared() {
        machine.clear()
    }
}