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

package com.motorro.statemachine.register.model.state

import com.motorro.statemachine.register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

open class BaseStateTest {
    protected val stateMachine: MachineMock = MachineMock()
    protected val context: MockContext = MockContext()
    protected val factory: MockFactory = context.factory
    protected val host: MockHost = context.host
    protected val resourceWrapper: MockResourceWrapper = MockResourceWrapper()

    @BeforeTest
    @OptIn(ExperimentalCoroutinesApi::class)
    open fun before() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    @OptIn(ExperimentalCoroutinesApi::class)
    open fun after() {
        Dispatchers.resetMain()
    }
}