package com.motorro.statemachine.commonregister.model.data

import com.motorro.statemachine.commonapi.data.RegistrationDataState


/**
 * Login flow inter-state data
 */
data class RegisterDataState(
    val commonData: RegistrationDataState,
    val password: String? = null
)