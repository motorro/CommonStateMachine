package com.motorro.statemachine.welcome.usecase

import com.motorro.statemachine.NETWORK_DELAY
import com.motorro.statemachine.commonapi.coroutines.DispatcherProvider
import com.motorro.statemachine.welcome.data.BAD
import com.motorro.statemachine.welcome.data.GOOD
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Checks if user is registered
 */
@ViewModelScoped
class CheckEmail @Inject constructor(private val dispatchers: DispatcherProvider) {
    /**
     * Checks if user is registered
     * @param email User to check
     */
    suspend operator fun invoke(email: String): Boolean = withContext(dispatchers.default) {
        val exists = checkUserExists(email)
        withContext(dispatchers.main) { exists }
    }

    private suspend fun checkUserExists(email: String): Boolean {
        delay(NETWORK_DELAY)
        return REGISTERED_USERS.contains(email.trim().lowercase())
    }

    companion object {
        /**
         * Registered users which switch to login flow
         */
        private val REGISTERED_USERS = setOf(
            GOOD,
            BAD
        )
    }
}