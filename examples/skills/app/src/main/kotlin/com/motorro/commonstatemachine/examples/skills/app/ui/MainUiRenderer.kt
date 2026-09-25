package com.motorro.commonstatemachine.examples.skills.app.ui

import com.motorro.commonstatemachine.examples.skills.app.data.MainUiState
import com.motorro.commonstatemachine.examples.skills.auth.api.AuthUiState
import org.koin.core.annotation.Factory

/**
 * Main UI renderer
 */
internal interface MainUiRenderer {
    fun renderSplash(): MainUiState
    fun renderAuth(child: AuthUiState): MainUiState
    fun renderContent(): MainUiState

    @Factory
    class Impl : MainUiRenderer {
        override fun renderSplash() = MainUiState.Splash

        override fun renderAuth(child: AuthUiState) = MainUiState.Auth(child)

        override fun renderContent(): MainUiState = MainUiState.Friends
    }
}