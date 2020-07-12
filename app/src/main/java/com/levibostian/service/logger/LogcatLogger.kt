package com.levibostian.service.logger

import android.os.Bundle
import android.util.Log
import com.levibostian.Env
import com.levibostian.extensions.isDevelopment
import java.util.*

class LogcatLogger: DebugLogger() {

    companion object {
        val TAG = Env.appName.toUpperCase(Locale.ENGLISH)
    }

    override fun setUserId(id: String?) {
        if (id == null) Log.d(TAG, "User logged out")
        else Log.d(TAG, "User logged in. id $id")
    }

    override fun appEventOccurred(event: ActivityEvent, extras: Map<ActivityEventParamKey, Any>?, average: Double?) {
        Log.d(TAG, "event: ${event.name}, extras: ${extras.toString()}")
    }

    override fun setUserProperty(key: UserPropertyKey, value: String) {
        Log.d(TAG, "Property, ${key.name}:$value")
    }

    override fun logDebug(message: String, extras: Bundle?) {
        Log.d(TAG, "$message, extras: ${extras.toString()}")
    }

    override fun logError(error: Throwable) {
        Log.e(TAG, "ERROR: ${error.message}", error)
        error.printStackTrace()

        // Throw because during dev mode, it's best to catch these errors to fix them.
        if (Env.isDevelopment) throw error
    }

}