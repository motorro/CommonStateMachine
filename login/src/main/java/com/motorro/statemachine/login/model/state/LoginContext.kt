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

import com.motorro.statemachine.commonapi.welcome.model.state.WelcomeFeatureHost
import com.motorro.statemachine.login.model.LoginRenderer


/**
 * Context for login flow
 */
interface LoginContext {
    /**
     * State factory
     */
    val factory: LoginStateFactory

    /**
     * Flow host
     */
    val host: WelcomeFeatureHost

    /**
     * UI-state renderer
     */
    val renderer: LoginRenderer
}