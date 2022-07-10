package com.motorro.statemachine.registration.data

/**
 * Registration data state
 * @property email Registration email
 * @property password Password
 */
data class RegistrationDataState(
    val email: String? = null,
    val password: String? = null
)