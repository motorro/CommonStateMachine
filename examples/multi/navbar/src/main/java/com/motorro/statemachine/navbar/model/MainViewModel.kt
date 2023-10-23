package com.motorro.statemachine.navbar.model

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.statemachine.navbar.model.data.NavbarGesture
import com.motorro.statemachine.navbar.model.data.NavbarUiState
import com.motorro.statemachine.navbar.model.state.NavbarState
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val machine = FlowStateMachine(NavbarUiState(emptyList(), null)) {
        NavbarState()
    }

    val uiState: StateFlow<NavbarUiState> get() = machine.uiState

    fun update(gesture: NavbarGesture) = machine.process(gesture)

    override fun onCleared() {
        machine.clear()
    }
}