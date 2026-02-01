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

package com.motorro.statemachine.books.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.motorro.statemachine.androidcore.compose.Loading
import com.motorro.statemachine.books.app.R
import com.motorro.statemachine.books.app.api.MainGesture
import com.motorro.statemachine.books.app.api.MainGesture.Book
import com.motorro.statemachine.books.app.api.MainGesture.BookSelected
import com.motorro.statemachine.books.app.api.MainUiState
import com.motorro.statemachine.books.book.ui.BookScreen
import kotlin.reflect.KClass

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun MainScreen(state: MainUiState, onGesture: (MainGesture) -> Unit, modifier: Modifier = Modifier) {
    AnimatedContent(
        targetState = state,
        modifier = modifier,
        contentKey = { it.contentKey },
        transitionSpec = mainTransitionSpec
    ) { targetState ->
        when (targetState) {
            is MainUiState.List -> MainScreenScaffold(targetState, onGesture)
            is MainUiState.Book -> BookScreen(targetState.child) {
                onGesture(Book(it))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainScreenScaffold(state: MainUiState.List, onGesture: (MainGesture) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }
            )
        },
        content = {
            AnimatedContent(targetState = state, modifier = Modifier.padding(it)) { targetState ->
                when(targetState) {
                    MainUiState.List.Loading -> Loading()
                    is MainUiState.List.Master -> BookListView(targetState.items) {
                        onGesture(BookSelected(it))
                    }
                }
            }
        }
    )
}

private val KClass<*>.contentKey: Any? get() = simpleName

private val MainUiState.contentKey: Any? get() = when(this) {
    is MainUiState.List -> MainUiState.List::class.contentKey
    is MainUiState.Book -> MainUiState.Book::class.contentKey
}

private val mainTransitionSpec: AnimatedContentTransitionScope<MainUiState>.() -> ContentTransform = {
    when (initialState.contentKey to targetState.contentKey) {
        MainUiState.List::class.contentKey to MainUiState.Book::class.contentKey -> {
            slideInHorizontally(
                animationSpec = tween(220, delayMillis = 90),
                initialOffsetX = { fullWidth -> fullWidth }
            ) togetherWith slideOutHorizontally(
                animationSpec = tween(220, delayMillis = 90),
                targetOffsetX = { fullWidth -> -fullWidth }
            )
        }
        MainUiState.Book::class.contentKey to MainUiState.List::class.contentKey -> {
            slideInHorizontally(
                animationSpec = tween(220, delayMillis = 90),
                initialOffsetX = { fullWidth -> - fullWidth }
            ) togetherWith slideOutHorizontally(
                animationSpec = tween(220, delayMillis = 90),
                targetOffsetX = { fullWidth -> fullWidth }
            )
        }
        else -> ContentTransform(EnterTransition.None, ExitTransition.None)
    }
}