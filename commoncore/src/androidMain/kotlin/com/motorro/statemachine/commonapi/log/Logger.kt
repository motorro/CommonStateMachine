package com.motorro.statemachine.commonapi.log

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