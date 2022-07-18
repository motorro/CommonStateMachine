package com.motorro.statemachine.register.di

import com.motorro.statemachine.register.model.state.RegisterStateFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn

/**
 * Register feature entry point
 */
@EntryPoint
@InstallIn(RegisterComponent::class)
interface RegisterEntryPoint {
    /**
     * Register state factory
     */
    fun factory(): RegisterStateFactory
}