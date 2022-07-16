package com.motorro.statemachine.register.usecase

import com.motorro.statemachine.NETWORK_DELAY
import com.motorro.statemachine.commonapi.coroutines.DispatcherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Emulates user registration
 */
interface Registration {
    /**
     * Registers user
     * @param email Email to register
     * @param password Password to set
     */
    suspend operator fun invoke(email: String, password: String): Boolean

    class Impl(private val dispatchers: DispatcherProvider) {
        /**
         * Registers user
         * @param email Email to register
         * @param password Password to set
         */
        suspend operator fun invoke(email: String, password: String): Boolean = withContext(dispatchers.default) {
            delay(NETWORK_DELAY)
            withContext(dispatchers.main) { true }
        }
    }
}
