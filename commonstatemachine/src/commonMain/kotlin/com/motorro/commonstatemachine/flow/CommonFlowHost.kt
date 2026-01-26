package com.motorro.commonstatemachine.flow

/**
 * Common flow host
 */
interface CommonFlowHost<in R> {
    /**
     * Completes common flow
     */
    fun onComplete(result: R? = null)
}