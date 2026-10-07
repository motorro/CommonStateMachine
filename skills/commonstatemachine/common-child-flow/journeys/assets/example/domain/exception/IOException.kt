package com.motorro.commonstatemachine.skills.domain.exception

/**
 * IO exception
 */
class IOException(message: String, cause: Throwable? = null) : AppException(message, cause)