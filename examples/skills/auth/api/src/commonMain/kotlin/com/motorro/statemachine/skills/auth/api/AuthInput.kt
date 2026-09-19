package com.motorro.statemachine.skills.auth.api

/**
 * Initializing data for the feature flow
 * @property skippable If true, allows user to skip authentication
 */
data class AuthInput(val skippable: Boolean)