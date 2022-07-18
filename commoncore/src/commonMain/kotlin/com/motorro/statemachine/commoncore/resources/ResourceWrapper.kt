package com.motorro.statemachine.commoncore.resources

/**
 * Wraps resource context
 */
interface ResourceWrapper {
    /**
     * Gets string resource
     */
    fun getString(resId: Int, vararg args: Any): String
}
