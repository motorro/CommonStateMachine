package com.motorro.statemachine.auth.api

/**
 * Feature flow result
 * @param authenticated Example of a property returned to a parent flow
 */
data class AuthInput(val authenticated: Boolean)