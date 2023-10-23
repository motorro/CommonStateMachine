/*
 * Copyright 2022 Nikolai Kotchetkov.
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

package com.motorro.statemachine.lce.ui

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.motorro.statemachine.androidcore.compose.Loading
import com.motorro.statemachine.lce.R
import com.motorro.statemachine.lce.data.LceGesture.*
import com.motorro.statemachine.lce.data.LceUiState
import com.motorro.statemachine.lce.model.LceViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun LceScreen(onExit: @Composable () -> Unit) {
    val model: LceViewModel = viewModel()
    val state = model.state.collectAsState(LceUiState.Loading)

    BackHandler(onBack = { model.process(Back) })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title)) },
                navigationIcon = {
                    IconButton(onClick = { model.process(Back) }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            when (val uiState = state.value) {
                LceUiState.Loading -> Loading()
                is LceUiState.ItemList -> ItemList(
                    state = uiState,
                    onItemClicked = { model.process(ItemClicked(it)) }
                )
                is LceUiState.Error -> LoadError(
                    state = uiState,
                    onRetry = { model.process(Retry) },
                    onBack = { model.process(Back) },
                    onExit = { model.process(Exit) }
                )
                is LceUiState.Item -> ItemDetails(state = uiState)
                LceUiState.Terminated -> onExit()
            }
        }
    }
}