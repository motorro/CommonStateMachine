package com.motorro.statemachine.parallel.model.data

import com.motorro.statemachine.multi.data.TimerGesture

/**
 * Application gestures
 */
sealed class ParallelGesture {
    /**
     * Top timer gesture
     */
    data class Top(val gesture: TimerGesture) : ParallelGesture()

    /**
     * Bottom timer gesture
     */
    data class Bottom(val gesture: TimerGesture) : ParallelGesture()
}