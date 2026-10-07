package com.motorro.commonstatemachine.skills.usecase.authenticate

import com.motorro.commonstatemachine.skills.domain.authenticate.AuthenticateWithPassword
import com.motorro.commonstatemachine.skills.domain.exception.AuthenticationException
import com.motorro.commonstatemachine.skills.domain.session.SessionManager
import com.motorro.commonstatemachine.skills.domain.session.data.Session
import com.motorro.commonstatemachine.skills.domain.session.data.Username
import com.motorro.commonstatemachine.skills.usecase.Fixtures.Authentication
import com.motorro.commonstatemachine.skills.usecase.Fixtures.NETWORK_DELAY
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import org.koin.core.annotation.Factory

@Factory
internal class AuthenticateWithPasswordImpl(private val sessionManager: SessionManager) : AuthenticateWithPassword {
    override suspend fun invoke(username: Username, password: String) {
        Napier.i { "Authenticating: ${username.value}..." }
        delay(NETWORK_DELAY)
        if (Authentication.USERNAME == username && Authentication.PASSWORD == password) {
            sessionManager.update(Session.Active(username))
        } else {
            Napier.w { "Authentication failed" }
            throw AuthenticationException("Invalid login or password")
        }
    }
}