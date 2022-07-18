package com.motorro.statemachine.commoncore.log

expect object Logger {
    fun d(message: String?, vararg args: Any?)

    fun i(message: String?, vararg args: Any?)

    fun w(message: String?, vararg args: Any?)

    fun w(t: Throwable?, message: String?, vararg args: Any?)

    fun w(t: Throwable?)

    fun e(message: String?, vararg args: Any?)

    fun e(t: Throwable?, message: String?, vararg args: Any?)

    fun e(t: Throwable?)
}