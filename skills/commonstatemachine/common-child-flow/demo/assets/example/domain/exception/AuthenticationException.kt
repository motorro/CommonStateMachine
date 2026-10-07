package com.motorro.commonstatemachine.skills.domain.exception

/**
 * Authentication exception
 */
class AuthenticationException(message: String, cause: Throwable? = null) : AppException(message, cause)