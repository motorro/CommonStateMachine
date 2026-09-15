package com.motorro.commonstatemachine.skills.domain.usecase

import com.motorro.commonstatemachine.skills.domain.entity.PasswordRequirements

/**
 * Loads password requirements
 */
interface GetPasswordRequirements {
    /**
     * Loads password requirements
     */
    suspend operator fun invoke(): PasswordRequirements
}