package com.app.service.logger

import android.os.Bundle
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.util.*

class CrashlyticsLogger : DebugLogger() {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    override fun setUserId(id: String?) {
        // To logout the user, set to blank string. Resource: https://firebase.google.com/docs/crashlytics/customize-crash-reports?platform=android#set_user_identifiers
        crashlytics.setUserId(id ?: "")
    }

    override fun appEventOccurred(event: ActivityEvent, extras: Map<ActivityEventParamKey, Any>?, average: Double?) {
        crashlytics.log("event: ${event.name.toLowerCase(Locale.ENGLISH)}, extras: $extras")
    }

    override fun setUserProperty(key: UserPropertyKey, value: String) {
        crashlytics.setCustomKey(key.name, value)
    }

    override fun logDebug(message: String, extras: Bundle?) {
        crashlytics.log("$message, extras: $extras")
    }

    override fun logError(error: Throwable) {
        crashlytics.recordException(error)
    }
}
