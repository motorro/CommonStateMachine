package com.motorro.statemachine.login.data

import com.motorro.statemachine.welcome.data.WelcomeDataState


/**
 * Login flow inter-state data
 */
data class LoginDataState(
    val commonData: WelcomeDataState,
    val password: String? = null
)