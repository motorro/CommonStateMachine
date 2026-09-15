package com.motorro.statemachine.skills.auth.implementation.state

import com.motorro.commonstatemachine.skills.domain.exception.AppException
import com.motorro.statemachine.skills.auth.api.AuthInput
import com.motorro.statemachine.skills.auth.api.AuthResult
import com.motorro.statemachine.skills.auth.implementation.data.AuthStateData

/**
 * State-factory interface
 * Abstracts the machine state from the state creation
 */
internal interface AuthStateFactory {
    /**
     * Creates a flow preloading state.
     * The interstate data will be created by the state.
     * @param input Initialization params
     */
    fun preloading(input: AuthInput): AuthState

    /**
     * Creates a preload error state.
     * Again, there's no interstate data here as the preloading failed.
     * @param input Initialization params
     * @param error Error that occurred during the initialization
     */
    fun preloadError(input: AuthInput, error: AppException): AuthState

    /**
     * Creates authentication form state.
     * Now we have the interstate data, pass it as a parameter.
     * @param data Interstate data
     */
    fun form(data: AuthStateData): AuthState

    /**
     * Creates the authentication state.
     * @param data Interstate data
     */
    fun authenticating(data: AuthStateData): AuthState

    /**
     * Creates the termination state that finishes the flow.
     * All interstate data is lost as not needed anymore.
     * @param result Flow result
     */
    fun terminated(result: AuthResult): AuthState
}