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

import com.motorro.commonstatemachine.flow.viewmodel.data.BaseFlowGesture
import com.motorro.commonstatemachine.flow.viewmodel.data.BaseFlowUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CommonFlowViewModelTest {

    private companion object {
        const val INIT = 1
        const val RESULT = "result"
    }

    private lateinit var dispatcher: TestDispatcher
    private lateinit var api: TestDataApi
    private lateinit var model: CommonFlowViewModel<TestGesture, TestUiState, Int, String?>

    @BeforeTest
    fun init() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)

        api = TestDataApi()
        model = CommonFlowViewModel(api, INIT)
    }

    @AfterTest
    fun deinit() {
        Dispatchers.resetMain()
    }

    private fun test(block: suspend TestScope.() -> Unit) = runTest(
        dispatcher,
        testBody = block
    )

    @Test
    fun startsApiAndWrapsUiState() = test {
        assertEquals(INIT, api.state.init)
        assertEquals(
            BaseFlowUiState.Child(TestUiState.RUNNING, true),
            model.uiState.value
        )
    }

    @Test
    fun passesChildGesture() = test {
        model.process(BaseFlowGesture.Child(TestGesture.Stop(RESULT)))
        assertEquals(BaseFlowUiState.Terminated(RESULT), model.uiState.value)
    }

    @Test
    fun mapsBackGesture() = test {
        model.process(BaseFlowGesture.Back)
        assertEquals(BaseFlowUiState.Terminated(null), model.uiState.value)
    }
}