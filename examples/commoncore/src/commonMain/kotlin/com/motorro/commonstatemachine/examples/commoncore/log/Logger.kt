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

package com.motorro.commonstatemachine.examples.commoncore.log

import io.github.aakira.napier.LogLevel
import io.github.aakira.napier.Napier

object Logger {

    private fun napier(logLevel: LogLevel, throwable: Throwable?, message: String?, args: Array<out Any?>?) {
        val toLog = buildString {
            message?.let { append(it) }
            throwable?.let {
                if (isNotEmpty()) {
                    append(" ")
                }
                append(it)
            }
            args?.takeIf { it.isNotEmpty() }?.let {
                if (isNotEmpty()) {
                    append(" ")
                }
                append(it.joinToString(prefix = "(", postfix = ")"))
            }
        }
        Napier.log(logLevel, throwable =throwable, message = toLog)
    }

    fun d(message: String?, vararg args: Any?) = napier(LogLevel.DEBUG, null, message, args)

    fun i(message: String?, vararg args: Any?) = napier(LogLevel.INFO, null, message, args)

    fun w(message: String?, vararg args: Any?) = napier(LogLevel.WARNING, null, message, args)

    fun w(t: Throwable?, message: String?, vararg args: Any?) = napier(LogLevel.WARNING, t, message, args)

    fun w(t: Throwable?) = napier(LogLevel.WARNING, t, null, null)

    fun e(message: String?, vararg args: Any?) = napier(LogLevel.ERROR, null, message, args)

    fun e(t: Throwable?, message: String?, vararg args: Any?) = napier(LogLevel.ERROR, t, message, args)

    fun e(t: Throwable?) = napier(LogLevel.ERROR, null, null, null)
}