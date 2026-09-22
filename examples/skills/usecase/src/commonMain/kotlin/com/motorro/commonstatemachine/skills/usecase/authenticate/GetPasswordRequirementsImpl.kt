package com.motorro.commonstatemachine.skills.usecase.authenticate

import com.motorro.commonstatemachine.skills.domain.authenticate.GetPasswordRequirements
import com.motorro.commonstatemachine.skills.domain.authenticate.data.PasswordRequirements
import com.motorro.commonstatemachine.skills.usecase.Fixtures
import com.motorro.commonstatemachine.skills.usecase.Fixtures.NETWORK_DELAY
import kotlinx.coroutines.delay
import org.koin.core.annotation.Factory

@Factory
internal class GetPasswordRequirementsImpl : GetPasswordRequirements {
    override suspend fun invoke(): PasswordRequirements {
        delay(NETWORK_DELAY)
        return Fixtures.Authentication.REQUIREMENTS
    }
}