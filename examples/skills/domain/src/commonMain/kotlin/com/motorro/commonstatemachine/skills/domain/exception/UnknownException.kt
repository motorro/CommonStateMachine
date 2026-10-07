package com.motorro.commonstatemachine.skills.domain.exception

/**
 * Unknown exception
 */
class UnknownException(message: String, cause: Throwable? = null) : AppException(message, cause)