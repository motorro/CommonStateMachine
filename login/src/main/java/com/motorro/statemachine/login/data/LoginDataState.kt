package com.motorro.statemachine.login.data

import com.motorro.statemachine.registrationapi.data.RegistrationDataState

/**
 * Login flow inter-state data
 */
data class LoginDataState(
    val commonData: RegistrationDataState,
    val password: String? = null
)