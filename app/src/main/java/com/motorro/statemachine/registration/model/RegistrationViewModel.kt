package com.motorro.statemachine.registration.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.LiveDataStateMachine
import com.motorro.statemachine.registration.data.RegistrationGesture
import com.motorro.statemachine.registration.data.RegistrationUiState
import com.motorro.statemachine.registration.model.state.RegistrationStateFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val factory: RegistrationStateFactory) : ViewModel() {
    /**
     * State machine
     */
    private val stateMachine = LiveDataStateMachine(::initializeStateMachine)

    /**
     * Creates initializing state
     */
    private fun initializeStateMachine(): CommonMachineState<RegistrationGesture, RegistrationUiState> {
        Timber.d("Initializing state machine...")
        return factory.preload()
    }

    /**
     * UI State
     */
    val state: LiveData<RegistrationUiState> = stateMachine.uiState

    /**
     * Updates state with UI gesture
     * @param gesture UI gesture to proceed
     */
    fun process(gesture: RegistrationGesture) {
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