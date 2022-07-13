package com.motorro.statemachine.login.di

import com.motorro.statemachine.login.model.state.LoginStateFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn

/**
 * Login feature entry point
 */
@EntryPoint
@InstallIn(LoginComponent::class)
interface LoginEntryPoint {
    /**
     * Login state factory
     */
    fun factory(): LoginStateFactory
}