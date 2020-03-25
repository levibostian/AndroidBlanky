package com.levibostian.service.logger

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

// List of pre-defined Firebase events: https://firebase.google.com/docs/reference/android/com/google/firebase/analytics/FirebaseAnalytics.Event
class FirebaseLogger(context: Context): Logger {

    private val firebaseAnalytics: FirebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }

    override fun enteredScreen(activity: Activity, screenName: String) {
        firebaseAnalytics.setCurrentScreen(activity, screenName, null)
    }

    override fun loggedIn(id: String, method: Logger.LoginMethod) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.METHOD, method.name)
        }

        firebaseAnalytics.setUserId(id)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
    }

    override fun loggedOut() {
        firebaseAnalytics.logEvent("Logout", null)
        firebaseAnalytics.setUserId(null)
    }

    override fun performedEvent(event: Logger.ActivityEvent, bundle: Bundle?) {
        firebaseAnalytics.logEvent(event.name, bundle)
    }

    override fun httpRequest(method: String, url: String) {
        // No need to log this to analytics.
    }

    override fun httpSuccess(method: String, url: String) {
        // No need to log this to analytics.
    }

    override fun httpFail(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?) {
        // No need to log this to analytics.
    }

    override fun errorOccurred(error: Throwable) {
        // No need to log this to analytics.
    }

}