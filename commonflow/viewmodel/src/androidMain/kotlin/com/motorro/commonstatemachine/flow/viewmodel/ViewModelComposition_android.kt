/*
 * Copyright 2026 Nikolai Kotchetkov.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motorro.commonstatemachine.flow.viewmodel

import android.view.View
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.CreationExtras

/**
 * Builds state machine composition
 * Ensure that [AppCompatActivity] has correct view model factory
 * @param extrasProducer optional extras producer
 * @param setResult implement to set the activity result or do any result processing
 * @param navigationBackHandler optional back navigation handler. Implement to pass back navigation to the view model
 * @param content content block
 */
fun <G: Any,  U: Any, R> AppCompatActivity.setStateMachineContent(
    extrasProducer: (() -> CreationExtras)? = null,
    setResult: (R?) -> Unit = { },
    navigationBackHandler: @Composable (Boolean, () -> Unit) -> Unit = { enabled, onBack ->
        BackHandler(enabled = enabled) { onBack() }
    },
    content: @Composable (U, (G) -> Unit) -> Unit
) {
    setContent {
        val viewModel: CommonFlowViewModel<G, U, *, R> by viewModels(extrasProducer = extrasProducer)
        CommonFlowComposition(
            viewModel = viewModel,
            navigationBackHandler = navigationBackHandler,
            content = content,
            finish = { result ->
                if (null != result) {
                    setResult(result)
                }
                finish()
            }
        )
    }
}

/**
 * Builds state machine composable
 * Ensure that [Fragment] has correct view model factory
 * @param onFinish called when the flow is finished
 * @param extrasProducer optional extras producer
 * @param navigationBackHandler optional back navigation handler. Implement to pass back navigation to the view model
 * @param content content block
 */
fun < G: Any,  U: Any, R> Fragment.createStateMachineView(
    onFinish: (R?) -> Unit,
    extrasProducer: (() -> CreationExtras)? = null,
    navigationBackHandler: @Composable (Boolean, () -> Unit) -> Unit = { enabled, onBack ->
        BackHandler(enabled = enabled) { onBack() }
    },
    content: @Composable (U, (G) -> Unit) -> Unit
): View = ComposeView(requireContext()).apply {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

    setContent {
        val viewModel: CommonFlowViewModel<G, U, *, R> by viewModels(extrasProducer = extrasProducer)
        CommonFlowComposition(
            viewModel = viewModel,
            navigationBackHandler = navigationBackHandler,
            content = content,
            finish = onFinish
        )
    }
}
