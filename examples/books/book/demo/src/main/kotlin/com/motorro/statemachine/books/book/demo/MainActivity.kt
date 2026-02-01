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

package com.motorro.statemachine.books.book.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.motorro.commonstatemachine.flow.viewmodel.setStateMachineContent
import com.motorro.statemachine.androidcore.ui.theme.CommonStateMachineTheme
import com.motorro.statemachine.books.book.api.BookGesture
import com.motorro.statemachine.books.book.api.BookInput
import com.motorro.statemachine.books.book.api.BookUiState
import com.motorro.statemachine.books.book.ui.BookScreen
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setStateMachineContent<BookGesture, BookUiState, Unit, MainViewModel>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<MainViewModel.Factory> {
                    it.create(BookInput(BOOK.id, BOOK.title))
                }
            },
            content = { state, onGesture ->
                CommonStateMachineTheme {
                    BookScreen(state, onGesture)
                }
            }
        )
    }
}
