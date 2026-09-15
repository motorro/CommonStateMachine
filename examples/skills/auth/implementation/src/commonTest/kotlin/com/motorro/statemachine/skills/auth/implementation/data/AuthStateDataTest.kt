package com.motorro.statemachine.skills.auth.implementation.data

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class AuthStateDataTest {
    @Test
    fun returnsTrueIfValid() {
        val data = AuthStateData(
            input = INPUT,
            passwordRequirements = PASSWORD_REQUIREMENTS,
            username = "user",
            password = "password",
        )
        assertTrue(data.isValidToAuthenticate())
    }

    @Test
    fun returnsFalseIfUsernameEmpty() {
        val data = AuthStateData(
            input = INPUT,
            passwordRequirements = PASSWORD_REQUIREMENTS,
            username = "",
            password = "password",
        )
        assertFalse(data.isValidToAuthenticate())
    }

    @Test
    fun returnsFalseIfUsernameBlank() {
        val data = AuthStateData(
            input = INPUT,
            passwordRequirements = PASSWORD_REQUIREMENTS,
            username = " ",
            password = "password",
        )
        assertFalse(data.isValidToAuthenticate())
    }

    @Test
    fun returnsFalseIfPasswordInvalid() {
        val data = AuthStateData(
            input = INPUT,
            passwordRequirements = PASSWORD_REQUIREMENTS,
            username = "user",
            password = "short", // PASSWORD_REQUIREMENTS regex is ^.{8,}$
        )
        assertFalse(data.isValidToAuthenticate())
    }
}
