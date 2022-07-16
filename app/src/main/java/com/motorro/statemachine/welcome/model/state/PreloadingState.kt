package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import com.motorro.statemachine.welcome.usecase.Preload
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Preloads 'data' to use in [TermsAndConditionsState]
 * Transfers loaded greeting through factory
 */
class PreloadingState(context: WelcomeContext, private val preload: Preload) : WelcomeState(context) {

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(WelcomeUiState.Loading)
        stateScope.launch {
            val greeting = preload()
            Timber.d("Transferring to welcome screen...")
            setMachineState(factory.welcome(greeting))
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: WelcomeGesture) = when (gesture) {
        is WelcomeGesture.Back -> onBack()
        else -> super.doProcess(gesture)
    }

    /**
     * Exits app
     */
    private fun onBack() {
        Timber.d("Terminating...")
        setMachineState(factory.terminate())
    }

    @ViewModelScoped
    class Factory @Inject constructor(private val preload: Preload) {
        operator fun invoke(context: WelcomeContext) = PreloadingState(context, preload)
    }
}