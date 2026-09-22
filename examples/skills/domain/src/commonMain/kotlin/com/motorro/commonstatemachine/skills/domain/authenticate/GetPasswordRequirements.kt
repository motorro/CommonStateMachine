package com.motorro.commonstatemachine.skills.domain.authenticate

import com.motorro.commonstatemachine.skills.domain.authenticate.data.PasswordRequirements

/**
 * Loads password requirements
 */
interface GetPasswordRequirements {
    /**
     * Loads password requirements
     */
    suspend operator fun invoke(): PasswordRequirements
}