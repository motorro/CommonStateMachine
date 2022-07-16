package com.motorro.statemachine.login.usecase

import com.motorro.statemachine.NETWORK_DELAY
import com.motorro.statemachine.commonapi.coroutines.DispatcherProvider
import com.motorro.statemachine.login.di.LoginScope
import com.motorro.statemachine.welcome.data.BAD
import com.motorro.statemachine.welcome.data.GOOD
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Emulates user's credentials check
 */
@LoginScope
class CheckCredentials @Inject constructor(private val dispatchers: DispatcherProvider) {
    /**
     * Tries to log-in user
     * @param email User to check
     * @param password Password to check
     */
    suspend operator fun invoke(email: String, password: String): Boolean = withContext(dispatchers.default) {
        val valid = checkCredentials(email)
        withContext(dispatchers.main) { valid }
    }

    private suspend fun checkCredentials(email: String): Boolean {
        delay(NETWORK_DELAY)
        return true == REGISTERED_USERS[email.trim().lowercase()]
    }

    companion object {
        /**
         * Users than can or can not login
         * Any password will do
         */
        private val REGISTERED_USERS = mapOf(
            BAD to false,
            GOOD to true
        )
    }
}