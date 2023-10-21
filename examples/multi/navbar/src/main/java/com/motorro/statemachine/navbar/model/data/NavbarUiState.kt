package com.motorro.statemachine.navbar.model.data

import androidx.compose.runtime.Immutable
import com.motorro.commonstatemachine.multi.MachineKey
import com.motorro.statemachine.timer.data.TimerKey
import com.motorro.statemachine.timer.data.TimerUiState

/**
 * Common state for all active machines
 */
@Immutable
data class NavbarUiState(
    val timers: List<Pair<TimerKey, TimerUiState>>,
    val active: MachineKey<*, *>?
)