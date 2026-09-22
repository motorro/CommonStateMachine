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

package com.motorro.statemachine.skills.app.state

import com.motorro.commonstatemachine.flow.data.CommonFlowHost
import com.motorro.statemachine.skills.app.ui.MainUiRenderer
import org.koin.core.annotation.Factory
import org.koin.core.annotation.InjectedParam

/**
 * Main state factory
 */
internal interface MainStateFactory {
    fun init(): MainState
    fun auth(): MainState
    fun content(): MainState
    fun terminated(): MainState

    @Factory
    class Impl(
        @InjectedParam flowHost: CommonFlowHost<Unit>,
        renderer: MainUiRenderer,
        private val checkingAuthenticationFactory: CheckingAuthentication.StateFactory,
        private val contentFactory: ContentState.StateFactory,
        private val authFactory: AuthProxyState.StateFactory
    ) : MainStateFactory {

        private val context = object : MainContext {
            override val flowHost: CommonFlowHost<Unit> = flowHost
            override val factory: MainStateFactory get() = this@Impl
            override val renderer: MainUiRenderer = renderer
        }

        override fun init() = checkingAuthenticationFactory.create(context)

        override fun auth() = authFactory.create(context)

        override fun content() = contentFactory.create(context)

        override fun terminated() = object : MainState() {
            override fun doStart() {
                context.flowHost.onComplete(Unit)
            }
        }
    }
}
