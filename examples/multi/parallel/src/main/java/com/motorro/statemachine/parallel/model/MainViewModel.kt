package com.motorro.statemachine.parallel.model

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.statemachine.multi.data.TimerUiState
import com.motorro.statemachine.parallel.model.data.ParallelGesture
import com.motorro.statemachine.parallel.model.data.ParallelUiState
import com.motorro.statemachine.parallel.model.state.ParallelState
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

class MainViewModel : ViewModel() {
    private val machine = FlowStateMachine(ParallelUiState(TimerUiState.Stopped(Duration.ZERO), TimerUiState.Stopped(Duration.ZERO))) {
        ParallelState()
    }

    val uiState: StateFlow<ParallelUiState> get() = machine.uiState

    fun update(gesture: ParallelGesture) = machine.process(gesture)

    override fun onCleared() {
        machine.clear()
    }
}