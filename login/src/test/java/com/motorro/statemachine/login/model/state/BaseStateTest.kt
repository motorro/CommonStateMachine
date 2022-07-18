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

package com.motorro.statemachine.login.model.state

import androidx.annotation.CallSuper
import com.motorro.commonstatemachine.CommonStateMachine
import com.motorro.statemachine.commonapi.welcome.data.GOOD
import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
import com.motorro.statemachine.commoncore.resources.ResourceWrapper
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import com.motorro.statemachine.login.model.LoginRenderer
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

open class BaseStateTest {
    protected val stateMachine: CommonStateMachine<LoginGesture, LoginUiState> = mockk(relaxed = true)
    protected val factory: LoginStateFactory = mockk()
    protected val host: WelcomeFeatureHost = mockk()
    protected val renderer: LoginRenderer = mockk()
    protected val context: LoginContext = mockk()
    protected val resourceWrapper: ResourceWrapper = mockk()

    init {
        every { context.factory } returns factory
        every { context.host } returns host
        every { context.renderer } returns renderer
        every { renderer.renderPassword(any(), any()) } returns R_PASSWORD
        every { renderer.renderLoading(any()) } returns R_LOADING
        every { renderer.renderError(any(), any()) } returns R_ERROR
        every { resourceWrapper.getString(any(), *anyVararg()) } returns R_STRING
    }

    @Before
    @CallSuper
    @OptIn(ExperimentalCoroutinesApi::class)
    open fun before() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    @CallSuper
    @OptIn(ExperimentalCoroutinesApi::class)
    open fun after() {
        Dispatchers.resetMain()
    }
    protected companion object {
        const val R_STRING = "String"
        val R_PASSWORD = LoginUiState.PasswordEntry(GOOD, "password", true)
        val R_LOADING = LoginUiState.Loading
        val R_ERROR = LoginUiState.LoginError(R_PASSWORD, "Error")
    }
}