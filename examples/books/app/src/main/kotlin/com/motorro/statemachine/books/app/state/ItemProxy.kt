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

package com.motorro.statemachine.books.app.state

import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.statemachine.books.app.api.MainGesture
import com.motorro.statemachine.books.app.api.MainUiState
import com.motorro.statemachine.books.app.data.MainDataState
import com.motorro.statemachine.books.book.api.BookApi
import com.motorro.statemachine.books.book.api.BookGesture
import com.motorro.statemachine.books.book.api.BookInput
import com.motorro.statemachine.books.book.api.BookUiState
import com.motorro.statemachine.books.book.api.ItemFlowHost
import com.motorro.statemachine.commoncore.log.Logger
import javax.inject.Inject

internal class ItemProxy(
    private val context: MainContext,
    private val data: MainDataState,
    private val input: BookInput,
    private val api: BookApi
) : ProxyMachineState<MainGesture, MainUiState, BookGesture, BookUiState>(api.getDefaultUiState()) {

    private val flowHast = ItemFlowHost {
        Logger.d("Item flow host terminated")
        setMachineState(context.factory.bookList(data))
    }

    override fun init() = api.init(flowHast, input)

    override fun mapGesture(parent: MainGesture): BookGesture? = when(parent) {
        is MainGesture.Back -> api.getBackGesture()
        is MainGesture.Book -> parent.child
        else -> null
    }

    override fun mapUiState(child: BookUiState): MainUiState = MainUiState.Book(child)

    class Factory @Inject constructor(private val api: BookApi) {
        operator fun invoke(context: MainContext, data: MainDataState, input: BookInput) = ItemProxy(
            context,
            data,
            input,
            api
        )
    }
}