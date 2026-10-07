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

package com.motorro.commonstatemachine.skills.usecase.session

import com.motorro.commonstatemachine.skills.domain.session.SessionManager
import com.motorro.commonstatemachine.skills.domain.session.data.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single

@Single
internal class SessionManagerImpl() : SessionManager {
    override val session: Flow<Session> field = MutableStateFlow<Session>(Session.None)

    override suspend fun update(session: Session) {
        this.session.emit(session)
    }
}