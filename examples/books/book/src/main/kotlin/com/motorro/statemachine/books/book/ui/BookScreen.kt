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

package com.motorro.statemachine.books.book.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.motorro.statemachine.androidcore.compose.Error
import com.motorro.statemachine.androidcore.compose.Loading
import com.motorro.statemachine.books.book.R
import com.motorro.statemachine.books.book.api.BookGesture
import com.motorro.statemachine.books.book.api.BookUiState

@Composable
fun BookScreen(state: BookUiState, onGesture: (BookGesture) -> Unit) {
    when(state) {
        is BookUiState.Loading -> LoadingScreen(state, onGesture)
        is BookUiState.Content -> BookContentScreen(state, onGesture)
        is BookUiState.Error -> ErrorScreen(state, onGesture)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LoadingScreen(state: BookUiState.Loading, onGesture: (BookGesture) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.title) },
                navigationIcon = {
                    IconButton(onClick = { onGesture(BookGesture.Back) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.content_back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Loading(
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ErrorScreen(state: BookUiState.Error, onGesture: (BookGesture) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.title) },
                navigationIcon = {
                    IconButton(onClick = { onGesture(BookGesture.Back) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.content_back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Error(
            error = state.error,
            onDismiss = { onGesture(BookGesture.Confirm) },
            modifier = Modifier.padding(padding)
        )
    }
}
