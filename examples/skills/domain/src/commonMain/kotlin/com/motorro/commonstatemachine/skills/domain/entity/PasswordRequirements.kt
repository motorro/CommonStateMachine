package com.motorro.commonstatemachine.skills.domain.entity

/**
 * Password requirements
 * @property regex Regular expression to match the password
 * @property description Description of the password requirements
 */
data class PasswordRequirements(
    val regex: Regex,
    val description: String
)