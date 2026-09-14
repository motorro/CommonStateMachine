package com.motorro.statemachine.auth.implementation.usecase

import com.motorro.statemachine.auth.implementation.data.PasswordRequirements

/**
 * Loads password requirements
 */
interface GetPasswordRequirements {
    /**
     * Loads password requirements
     */
    suspend operator fun invoke(): PasswordRequirements
}