package com.motorro.statemachine.welcome.model.state

import com.motorro.statemachine.R
import com.motorro.statemachine.coroutines.DispatcherProvider
import com.motorro.statemachine.resources.ResourceWrapper
import com.motorro.statemachine.welcome.data.WelcomeGesture
import com.motorro.statemachine.welcome.data.WelcomeUiState
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Preloads 'data' to use in [TermsAndConditionsState]
 * Transfers loaded greeting through factory
 */
class PreloadingState(
    context: WelcomeContext,
    resourceWrapper: ResourceWrapper,
    private val dispatchers: DispatcherProvider
) : WelcomeState(context), ResourceWrapper by resourceWrapper {

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        setUiState(WelcomeUiState.Loading)
        stateScope.launch(dispatchers.default) {
            val greeting = getWelcomeGreeting()
            withContext(dispatchers.main) {
                Timber.d("Transferring to welcome screen...")
                setMachineState(factory.welcome(greeting))
            }
        }
    }

    /**
     * 'Loads' welcome greeting
     */
    private suspend fun getWelcomeGreeting(): String {
        delay(2000L)
        Timber.d("Loading complete")
        return getString(R.string.welcome_terms)
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
    class Factory @Inject constructor(
        private val resourceWrapper: ResourceWrapper,
        private val dispatchers: DispatcherProvider
    ) {
        operator fun invoke(context: WelcomeContext) = PreloadingState(
            context,
            resourceWrapper,
            dispatchers
        )
    }
}