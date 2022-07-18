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

package com.motorro.statemachine.register.model

import com.motorro.statemachine.commoncore.resources.ResourceWrapper
import com.motorro.statemachine.register.data.PasswordValidationError
import com.motorro.statemachine.register.data.PasswordValidationError.PASSWORD_LENGTH
import com.motorro.statemachine.register.data.PasswordValidationError.PASSWORD_MISMATCH

/**
 * Returns password error text
 */
actual fun ResourceWrapper.getPasswordError(error: PasswordValidationError): String = when(error) {
    PASSWORD_LENGTH -> "Password should be at least 6 chars long"
    PASSWORD_MISMATCH -> "Passwords mismatch"
}
