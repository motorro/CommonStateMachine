package com.motorro.statemachine.welcome.model

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.FlowStateMachine
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import com.motorro.statemachine.welcome.model.state.WelcomeStateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(private val factory: WelcomeStateFactory) : ViewModel() {
    /**
     * State machine
     */
    private val stateMachine = FlowStateMachine(::initializeStateMachine)

    /**
     * Creates initializing state
     */
    private fun initializeStateMachine(): CommonMachineState<WelcomeGesture, WelcomeUiState> {
        Timber.d("Initializing state machine...")
        return factory.preload()
    }

    /**
     * UI State
     */
    val state: SharedFlow<WelcomeUiState> = stateMachine.uiState

    /**
     * Updates state with UI gesture
     * @param gesture UI gesture to proceed
     */
    fun process(gesture: WelcomeGesture) {
        Timber.d("Gesture: %s", gesture)
        stateMachine.process(gesture)
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        stateMachine.clear()
    }
}