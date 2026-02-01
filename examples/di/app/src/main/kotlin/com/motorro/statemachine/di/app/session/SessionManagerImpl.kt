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

package com.motorro.statemachine.di.app.session

import com.motorro.statemachine.di.api.SessionManager
import com.motorro.statemachine.di.api.data.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

internal class SessionManagerImpl @Inject constructor() : SessionManager {
    private val _session = MutableStateFlow<Session>(Session.None)
    override val session: StateFlow<Session> get() = _session.asStateFlow()

    override suspend fun update(session: Session) {
        _session.emit(session)
    }
}