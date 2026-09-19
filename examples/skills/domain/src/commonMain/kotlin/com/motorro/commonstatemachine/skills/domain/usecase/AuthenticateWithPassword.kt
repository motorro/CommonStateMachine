package com.motorro.commonstatemachine.skills.domain.usecase

/**
 * Authenticates with a password
 */
interface AuthenticateWithPassword {
    /**
     * Authenticates with [username] and [password]
     * @param username Username
     * @param password Password
     */
    suspend operator fun invoke(username: String, password: String)
}