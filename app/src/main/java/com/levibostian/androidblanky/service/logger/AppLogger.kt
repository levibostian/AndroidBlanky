package com.levibostian.androidblanky.service.logger

import android.app.Activity
import android.os.Bundle
import javax.inject.Inject

class AppLogger(private val loggers: List<Logger>): Logger {

    override fun enteredScreen(activity: Activity, screenName: String) {
        loggers.forEach { it.enteredScreen(activity, screenName) }
    }

    override fun loggedIn(id: String, method: Logger.LoginMethod) {
        loggers.forEach { it.loggedIn(id, method) }
    }

    override fun loggedOut() {
        loggers.forEach { it.loggedOut() }
    }

    override fun performedEvent(event: Logger.ActivityEvent, bundle: Bundle?) {
        loggers.forEach { it.performedEvent(event, bundle) }
    }

    override fun httpRequest(method: String, url: String) {
        loggers.forEach { it.httpRequest(method, url) }
    }

    override fun httpSuccess(method: String, url: String) {
        loggers.forEach { it.httpSuccess(method, url) }
    }

    override fun httpFail(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?) {
        loggers.forEach { it.httpFail(method, url, code, reqHeaders, resHeaders, resBody) }
    }

    override fun errorOccurred(error: Throwable) {
        loggers.forEach { it.errorOccurred(error) }
    }

}