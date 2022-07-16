package com.motorro.statemachine.register.data

import com.motorro.statemachine.welcome.data.WelcomeDataState

/**
 * Login flow inter-state data
 */
data class RegisterDataState(
    val commonData: WelcomeDataState,
    val password: String? = null
)