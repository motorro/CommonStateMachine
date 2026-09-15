package com.motorro.commonstatemachine.skills.domain.exception

/**
 * Base domain exception
 * @property message Error message
 * @property cause Error cause if any
 */
abstract class AppException(final override val message: String, final override val cause: Throwable? = null) : RuntimeException() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppException

        if (message != other.message) return false
        if (cause != other.cause) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + (cause?.hashCode() ?: 0)
        return result
    }
}

/**
 * Converts exception to domain
 */
fun Throwable.toAppException(): AppException = when(this) {
    is AppException -> this
    else -> UnknownException(message ?: "UNKNOWN exception", this)
}