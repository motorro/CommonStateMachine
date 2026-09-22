package com.motorro.commonstatemachine.skills.domain.authenticate

import com.motorro.commonstatemachine.skills.domain.session.data.Username

/**
 * Authenticates with a password
 */
interface AuthenticateWithPassword {
    /**
     * Authenticates with [username] and [password]
     * @param username Username
     * @param password Password
     */
    suspend operator fun invoke(username: Username, password: String)
}