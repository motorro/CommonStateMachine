package com.motorro.statemachine.resources

/**
 * Wraps resource context
 */
interface ResourceWrapper {
    /**
     * Gets string resource
     */
    fun getString(resId: Int, vararg args: Any): String
}

/**
 * Uses resource wrapper
 */
interface UsesResourceWrapper {
    val resourceWrapper: ResourceWrapper
}
