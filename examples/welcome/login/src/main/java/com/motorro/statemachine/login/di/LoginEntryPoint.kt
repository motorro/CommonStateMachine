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

package com.motorro.statemachine.login.di

import com.motorro.statemachine.commonapi.welcome.model.state.FlowStarter
import com.motorro.statemachine.login.data.LoginGesture
import com.motorro.statemachine.login.data.LoginUiState
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn

/**
 * Login feature entry point
 */
@EntryPoint
@InstallIn(LoginComponent::class)
interface LoginEntryPoint {
    /**
     * Flow starter
     */
    fun flowStarter(): FlowStarter<LoginGesture, LoginUiState>
}