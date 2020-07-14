package com.levibostian.service.logger

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.extensions.camelToSnakeCase
import com.levibostian.extensions.toBundle
import java.util.*

class FirebaseLogger(context: Context) : Logger {

    private val firebaseAnalytics: FirebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }

    override fun setUserId(id: String?) {
        firebaseAnalytics.setUserId(id)
    }

    override fun appEventOccurred(event: ActivityEvent, extras: Map<ActivityEventParamKey, Any>?, average: Double?) {
        var extras = extras?.mapKeys {
            it.key.firebaseName
        }?.toMutableMap()

        average?.let { average ->
            if (extras == null) {
                extras = mutableMapOf()
            }

            extras!![FirebaseAnalytics.Param.VALUE] = average
        }

        firebaseAnalytics.logEvent(event.firebaseName, extras?.toBundle())
    }

    override fun setUserProperty(key: UserPropertyKey, value: String) {
        firebaseAnalytics.setUserProperty(key.firebaseName, value)
    }

    override fun breadcrumb(caller: Any, event: String, extras: Bundle?) {
        // No need to log this to analytics.
    }

    override fun httpRequestEvent(method: String, url: String, reqBody: String?) {
        // No need to log this to analytics.
    }

    override fun httpSuccessEvent(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?) {
        // No need to log this to analytics.
    }

    override fun httpFailEvent(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?) {
        // No need to log this to analytics.
    }

    override fun errorOccurred(error: Throwable) {
        // No need to log this to analytics.
    }
}

val UserPropertyKey.firebaseName: String
    get() = this.name.camelToSnakeCase().toLowerCase(Locale.ENGLISH)

/**
Firebase comes with a set of event names that you should use in your app, if it fits your app. In this function we convert our app's custom events to these built-in event names if they exist inside of Firebase's collection.
https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Param
 */
val ActivityEventParamKey.firebaseName: String
    get() {
        return when (this) {
            ActivityEventParamKey.Method -> FirebaseAnalytics.Param.METHOD
            else -> this.name.camelToSnakeCase().toLowerCase(Locale.ENGLISH)
        }
    }

/**
Firebase comes with a set of event names that you should use in your app, if it fits your app. In this function we convert our app's custom events to these built-in event names if they exist inside of Firebase's collection.
https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Event
 */
val ActivityEvent.firebaseName: String
    get() {
        return when (this) {
            ActivityEvent.Login -> FirebaseAnalytics.Event.LOGIN
            else -> this.name.camelToSnakeCase().toLowerCase(Locale.ENGLISH)
        }
    }
