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

import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.statemachine.books.app.api.MainGesture
import com.motorro.statemachine.books.app.api.MainUiState
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
internal abstract class BaseStateTest {
    protected lateinit var stateMachine: CommonStateMachine<MainGesture, MainUiState>
    protected lateinit var factory: MainFactory
    protected lateinit var context: MainContext
    protected lateinit var nextState: BaseMainState
    protected lateinit var dispatcher: TestDispatcher

    @Before
    fun init() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        stateMachine = mockk(relaxed = true)
        factory = mockk()
        context = object : MainContext {
            override val factory: MainFactory = this@BaseStateTest.factory
        }
        nextState = mockk(relaxed = true)
        doInit()
    }

    @After
    fun deinit() {
        Dispatchers.resetMain()
    }

    protected fun test(block: suspend TestScope.() -> Unit) = runTest(
        dispatcher,
        testBody = block
    )

    protected open fun doInit() = Unit
}