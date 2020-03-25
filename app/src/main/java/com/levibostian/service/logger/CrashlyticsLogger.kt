package com.levibostian.service.logger

import android.util.Log
import com.crashlytics.android.Crashlytics

class CrashlyticsLogger: DebugLogger() {

    override fun setUserProperty(name: String, property: String?) {
        Crashlytics.setString(name, property ?: "null")
    }

    override fun logEvent(tag: String, message: String) {
        Crashlytics.log(Log.DEBUG, tag, message)
    }

    override fun logError(error: Throwable) {
        Crashlytics.logException(error)
    }

}