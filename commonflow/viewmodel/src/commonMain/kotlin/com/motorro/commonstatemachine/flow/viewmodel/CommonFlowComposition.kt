package com.motorro.commonstatemachine.flow.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.motorro.commonstatemachine.flow.viewmodel.data.BaseFlowGesture
import com.motorro.commonstatemachine.flow.viewmodel.data.BaseFlowUiState

/**
 * Binds [CommonFlowViewModel] to the screen composition
 * @param G - gesture type
 * @param U - UI state type
 * @param R - result type
 * @param viewModel View-model instance
 * @param navigationBackHandler Back navigation handler slot. Call the supplied function when back is detected
 * @param content Content view. Accepts the UI state and gesture handler as parameters
 * @param finish Called when the flow is finished
 * @see CommonFlowViewModel
 */
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun < G: Any,  U: Any, R> CommonFlowComposition(
    viewModel: CommonFlowViewModel<G, U, *, R>,
    navigationBackHandler: @Composable (enabled: Boolean, onBack: () -> Unit) -> Unit = { _, _ -> },
    content: @Composable (U, (G) -> Unit) -> Unit,
    finish: (R?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val process = remember<(G) -> Unit> {
        { viewModel.process(BaseFlowGesture.Child(it)) }
    }

    navigationBackHandler(uiState.backHandlerEnabled) { viewModel.process(BaseFlowGesture.Back) }

    when (val state = uiState) {
        is BaseFlowUiState.Child -> content(state.child, process)
        is BaseFlowUiState.Terminated -> LaunchedEffect(Unit) {
            finish(state.result)
        }
    }
}