package com.motorro.statemachine.skills.auth.implementation.ui

import com.motorro.statemachine.skills.auth.implementation.data.AuthUiStateImpl
import com.motorro.statemachine.skills.auth.implementation.data.EMPTY_STATE
import com.motorro.statemachine.skills.auth.implementation.data.FATAL_ERROR
import com.motorro.statemachine.skills.auth.implementation.data.VALID_FORM_STATE
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AuthUiRendererImplTest {
    private val renderer = AuthUiRendererImpl()

    @Test
    fun rendersLoading() {
        assertEquals(AuthUiStateImpl.Loading, renderer.renderLoading())
    }

    @Test
    fun rendersForm() {
        val uiState = renderer.renderForm(VALID_FORM_STATE, isValid = true)
        val expected = AuthUiStateImpl.Form(
            username = VALID_FORM_STATE.username,
            password = VALID_FORM_STATE.password,
            passwordRequirements = VALID_FORM_STATE.passwordRequirements.description,
            loginEnabled = true,
            canSkip = true
        )
        assertEquals(expected, uiState)
    }

    @Test
    fun rendersFormInvalid() {
        val uiState = renderer.renderForm(EMPTY_STATE, isValid = false)
        val expected = AuthUiStateImpl.Form(
            username = EMPTY_STATE.username,
            password = EMPTY_STATE.password,
            passwordRequirements = EMPTY_STATE.passwordRequirements.description,
            loginEnabled = false,
            canSkip = true
        )
        assertEquals(expected, uiState)
    }

    @Test
    fun rendersFullScreenError() {
        val uiState = renderer.renderFullScreenError(FATAL_ERROR, canRetry = false)
        val expected = AuthUiStateImpl.Error(
            message = FATAL_ERROR.message,
            canRetry = false
        )
        assertEquals(expected, uiState)
    }

    @Test
    fun rendersFullScreenErrorWithRetry() {
        val uiState = renderer.renderFullScreenError(FATAL_ERROR, canRetry = true)
        val expected = AuthUiStateImpl.Error(
            message = FATAL_ERROR.message,
            canRetry = true
        )
        assertEquals(expected, uiState)
    }
}
