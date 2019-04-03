package com.levibostian.androidblanky.service.logger

import android.app.Activity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Log the many activities/events that can happen within the app.
 */
interface Logger {
    fun enteredScreen(activity: Activity, screenName: String)
    fun loggedIn(id: String, method: LoginMethod)
    fun loggedOut()
    fun performedEvent(event: ActivityEvent, bundle: Bundle?)

    fun httpRequest(method: String, url: String)
    fun httpSuccess(method: String, url: String)
    fun httpFail(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?)
    fun errorOccurred(error: Throwable)

    sealed class LoginMethod(val name: String) {
        class PasswordlessEmail: LoginMethod("passwordless_email")
    }

    sealed class ActivityEvent(val name: String) {
        class InstalledAppReferral(): ActivityEvent(FirebaseAnalytics.Event.CAMPAIGN_DETAILS)
        class EnteredUsername(): ActivityEvent("Entered_username")
    }

    sealed class Screen(val name: String) {
        class Home: Screen("Home")
    }

}