package com.motorro.statemachine.commoncore.log

actual object Logger {
    actual fun d(message: String?, vararg args: Any?) {
        console.log(message, *args)
    }

    actual fun i(message: String?, vararg args: Any?) {
        console.info(message, *args)
    }

    actual fun w(message: String?, vararg args: Any?) {
        console.warn(message, *args)
    }

    actual fun w(t: Throwable?, message: String?, vararg args: Any?) {
        console.warn(message, t, *args)
    }

    actual fun w(t: Throwable?) {
        console.warn(t)
    }

    actual fun e(message: String?, vararg args: Any?) {
        console.error(message, *args)
    }

    actual fun e(t: Throwable?, message: String?, vararg args: Any?) {
        console.error(message, t, *args)
    }

    actual fun e(t: Throwable?) {
        console.error(t)
    }
}