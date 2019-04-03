package com.levibostian.androidblanky.service.logger

import android.util.Log
import com.levibostian.androidblanky.BuildConfig
import com.levibostian.androidblanky.extensions.getStacktrace

class LogcatLogger: DebugLogger() {

    companion object {
        const val TAG = "APP_DEV"
    }

    override fun setUserProperty(name: String, property: String?) {
        logMessage("Set user property. name: $name, property: ${property ?: "null"}")
    }

    override fun logEvent(tag: String, message: String) {
        logMessage("Event. $tag: $message")
    }

    override fun logError(error: Throwable) {
        logMessage("ERROR: ${error.message}\n\n${error.getStacktrace()}")

        // Throw because during dev mode, it's best to catch these errors to fix them.
        if (BuildConfig.DEBUG) throw error
    }

    private fun logMessage(message: String) {
        if (BuildConfig.DEBUG) Log.d(TAG, message)
    }

}