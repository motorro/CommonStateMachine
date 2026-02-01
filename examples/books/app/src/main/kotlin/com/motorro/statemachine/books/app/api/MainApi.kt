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

package com.motorro.statemachine.books.app.api

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.flow.data.CommonFlowDataApi
import com.motorro.commonstatemachine.flow.data.CommonFlowHost
import com.motorro.statemachine.books.app.data.MainDataState
import com.motorro.statemachine.books.app.state.MainFactory
import javax.inject.Inject

/**
 * Item common flow
 */
class MainApi @Inject internal constructor(private val factory: MainFactory.Factory) : CommonFlowDataApi<MainGesture, MainUiState, Unit, Unit> {

    override fun init(flowHost: CommonFlowHost<Unit>, input: Unit): CommonMachineState<MainGesture, MainUiState> =
        factory.create(flowHost).bookList(MainDataState())

    override fun getDefaultUiState(): MainUiState = MainUiState.List.Loading
    override fun getBackGesture(): MainGesture = MainGesture.Back
}