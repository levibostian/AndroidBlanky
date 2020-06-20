package com.levibostian.service.logger

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsLogger: DebugLogger() {

    override fun setUserProperty(name: String, property: String?) {
        FirebaseCrashlytics.getInstance().setCustomKey(name, property ?: "null")
    }

    override fun logEvent(tag: String, message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }

    override fun logError(error: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(error)
    }

}