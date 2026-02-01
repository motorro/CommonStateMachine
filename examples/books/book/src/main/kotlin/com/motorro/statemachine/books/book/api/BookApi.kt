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

package com.motorro.statemachine.books.book.api

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.flow.data.CommonFlowDataApi
import com.motorro.commonstatemachine.flow.data.CommonFlowHost
import com.motorro.statemachine.books.book.state.BookFactory
import javax.inject.Inject

/**
 * Item common flow
 */
class BookApi @Inject internal constructor(private val factory: BookFactory.Factory) : CommonFlowDataApi<BookGesture, BookUiState, BookInput, Unit> {
    override fun init(flowHost: CommonFlowHost<Unit>, input: BookInput): CommonMachineState<BookGesture, BookUiState> =
        factory.create(flowHost).loading(input)

    override fun getDefaultUiState(): BookUiState = BookUiState.Loading("")
    override fun getBackGesture(): BookGesture = BookGesture.Back
}