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

package com.motorro.statemachine.commoncore.log

import timber.log.Timber

actual object Logger {
    actual fun d(message: String?, vararg args: Any?) {
        Timber.d(message, *args)
    }

    actual fun i(message: String?, vararg args: Any?) {
        Timber.i(message, *args)
    }

    actual fun w(message: String?, vararg args: Any?) {
        Timber.w(message, *args)
    }

    actual fun w(t: Throwable?, message: String?, vararg args: Any?) {
        Timber.w(t, message, *args)
    }

    actual fun w(t: Throwable?) {
        Timber.w(t)
    }

    actual fun e(message: String?, vararg args: Any?) {
        Timber.e(message, *args)
    }

    actual fun e(t: Throwable?, message: String?, vararg args: Any?) {
        Timber.e(t, message, *args)
    }

    actual fun e(t: Throwable?) {
        Timber.e(t)
    }
}