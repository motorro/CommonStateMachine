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

import com.motorro.statemachine.commonapi.welcome.data.WelcomeDataState
import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
import com.motorro.statemachine.commoncore.log.Logger
import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.model.RegistrationRenderer

/**
 * Login flow state factory
 */
interface RegisterStateFactory {
    /**
     * Creates a starting state
     * @param data Common data state
     */
    fun start(data: WelcomeDataState): RegisterState = passwordEntry(RegisterDataState(data))

    /**
     * Password entry screen
     * @param data Registration data state
     */
    fun passwordEntry(data: RegisterDataState): RegisterState

    /**
     * Registers user
     * @param data Data state
     */
    fun registering(data: RegisterDataState): RegisterState

    /**
     * [RegisterStateFactory] implementation
     */
    class Impl(
        host: WelcomeFeatureHost,
        renderer: RegistrationRenderer,
        private val createRegistration: RegistrationState.Factory
    ) : RegisterStateFactory {

        private val context: RegisterContext = object : RegisterContext {
            override val factory: RegisterStateFactory = this@Impl
            override val host: WelcomeFeatureHost = host
            override val renderer: RegistrationRenderer = renderer
        }

        /**
         * Password entry screen
         * @param data Registration data state
         */
        override fun passwordEntry(data: RegisterDataState): RegisterState {
            Logger.d("Creating 'Password entry'...")
            return PasswordEntryState(context, data)
        }

        /**
         * Registers user
         * @param data Data state
         */
        override fun registering(data: RegisterDataState): RegisterState {
            Logger.d("Creating 'Credentials check'...")
            return createRegistration(context, data)
        }
    }
}