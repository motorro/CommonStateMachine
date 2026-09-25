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

package com.motorro.commonstatemachine.examples.welcome.register.model.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.examples.welcome.register.RegisterFlowStarter
import com.motorro.commonstatemachine.examples.welcome.register.data.RegisterDataState
import com.motorro.commonstatemachine.examples.welcome.register.data.RegisterGesture
import com.motorro.commonstatemachine.examples.welcome.register.data.RegisterUiState

/**
 * Login flow state factory
 */
internal interface RegisterStateFactory : RegisterFlowStarter {
    /**
     * Creates a starting state
     * @param email Email to proceed with
     */
    override fun start(email: String): CommonMachineState<RegisterGesture, RegisterUiState> = passwordEntry(
        RegisterDataState(email)
    )

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

}

