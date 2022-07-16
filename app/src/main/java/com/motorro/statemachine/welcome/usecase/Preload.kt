package com.motorro.statemachine.welcome.usecase

import com.motorro.statemachine.NETWORK_DELAY
import com.motorro.statemachine.R
import com.motorro.statemachine.commonapi.coroutines.DispatcherProvider
import com.motorro.statemachine.commonapi.resources.ResourceWrapper
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Emulates data preload
 */
@ViewModelScoped
class Preload @Inject constructor(
    resourceWrapper: ResourceWrapper,
    private val dispatchers: DispatcherProvider,
): ResourceWrapper by resourceWrapper {
    /**
     * 'Loads' welcome greeting
     */
    suspend operator fun invoke(): String = withContext(dispatchers.default) {
        val greeting = loadGreeting()
        withContext(dispatchers.main) { greeting }
    }

    private suspend fun loadGreeting(): String {
        delay(NETWORK_DELAY)
        Timber.d("Loading complete")
        return getString(R.string.welcome_terms)
    }
}