package com.motorro.statemachine.navbar.model.data

import com.motorro.statemachine.multi.data.TimerGesture
import com.motorro.statemachine.multi.data.TimerKey

/**
 * Application gestures
 */
sealed class NavbarGesture {
    /**
     * Active page selected
     */
    data class ActiveSelected(val key: TimerKey) : NavbarGesture()

    /**
     * Timer gesture
     */
    data class Child(val key: TimerKey, val gesture: TimerGesture) : NavbarGesture()
}