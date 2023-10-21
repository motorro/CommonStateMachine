package com.motorro.statemachine.parallel.model.data

import androidx.compose.runtime.Immutable
import com.motorro.statemachine.multi.data.TimerUiState

/**
 * Common state for both active machines
 */
@Immutable
data class ParallelUiState(
    val top: TimerUiState,
    val bottom: TimerUiState
)