package com.levibostian.service.logger

import android.app.Activity
import android.os.Bundle

/**
 * [Logger] intended for development purposes. Leaving breadcrumbs for debugging.
 */
abstract class DebugLogger: Logger {

    companion object {
        const val USER_ID_TAG = "UserId"
        const val HTTP_EVENT_TAG = "HttpEvent"
    }

    override fun enteredScreen(activity: Activity, screenName: String) {
        logEvent("EnteredScreen", "Name: $screenName, from Activity: ${activity::class.java.simpleName}")
    }

    override fun loggedIn(id: String, method: Logger.LoginMethod) {
        setUserProperty(USER_ID_TAG, id)
        logEvent("LoggedIn", "method: ${method.name}")
    }

    override fun loggedOut() {
        logEvent("LoggedOut", "")
        setUserProperty(USER_ID_TAG, null)
    }

    override fun performedEvent(event: Logger.ActivityEvent, bundle: Bundle?) {
        logEvent(event.name, bundle.toString())
    }

    override fun httpRequest(method: String, url: String) {
        logEvent(HTTP_EVENT_TAG, "Request. method: $method, url: $url")
    }

    override fun httpSuccess(method: String, url: String) {
        logEvent(HTTP_EVENT_TAG, "Success! method: $method, url: $url")
    }

    override fun httpFail(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?) {
        val message = "Failed! method: $method, url: $url, code: $code, req headers: ${reqHeaders ?: "(none)" }, res headers: ${reqHeaders ?: "(none)" }, res body: ${resBody ?: "(none)" }"

        logEvent(HTTP_EVENT_TAG, message)
        logError(DebugHttpFailed("HTTP $message"))
    }

    class DebugHttpFailed(message: String): Throwable(message)

    override fun errorOccurred(error: Throwable) {
        logError(error)
    }

    abstract fun setUserProperty(name: String, property: String?)

    abstract fun logEvent(tag: String, message: String)

    abstract fun logError(error: Throwable)

}