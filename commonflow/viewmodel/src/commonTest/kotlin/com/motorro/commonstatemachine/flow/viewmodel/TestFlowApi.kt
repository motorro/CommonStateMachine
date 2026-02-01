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

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.flow.data.CommonFlowDataApi
import com.motorro.commonstatemachine.flow.data.CommonFlowHost

sealed class TestGesture {
    data object Back : TestGesture()
    data class Stop(val result: String?) : TestGesture()
}

enum class TestUiState {
    LOADING,
    RUNNING
}

class TestFlowState(
    private val flowHost: CommonFlowHost<String?>,
    val init: Int
) : CommonMachineState<TestGesture, TestUiState>() {

    var cleared = false

    override fun doStart() {
        setUiState(TestUiState.RUNNING)
    }

    override fun doProcess(gesture: TestGesture) {
        when (gesture) {
            is TestGesture.Back -> flowHost.onComplete(null)
            is TestGesture.Stop -> flowHost.onComplete(gesture.result)
        }
    }

    override fun doClear() {
        cleared = true
    }
}

class TestDataApi : CommonFlowDataApi<TestGesture, TestUiState, Int, String?> {
    lateinit var state: TestFlowState

    override fun init(flowHost: CommonFlowHost<String?>, input: Int): CommonMachineState<TestGesture, TestUiState> {
        return TestFlowState(flowHost, input).also { state = it }
    }

    override fun getDefaultUiState(): TestUiState = TestUiState.LOADING
    override fun getBackGesture(): TestGesture = TestGesture.Back
}