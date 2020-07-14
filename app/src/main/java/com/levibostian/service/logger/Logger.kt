package com.levibostian.service.logger

import android.os.Bundle

/**
 * Log the many activities/events that can happen within the app.
 *
 * The functions here are meant to be common between many different types of loggers. This file is meant to be generic enough that all loggers can inherit so there are no specific functions meant for 1 specific logger, as much as we can help it.
 */
interface Logger {
    fun setUserId(id: String?)

    // meant for analytics purposes
    // Note: `average` is meant for firebase analytics only used to track averages. Therefore, it's only good for tracking time/scores/periods of time See: https://firebase.googleblog.com/2017/02/firebase-analytics-quick-tip-value.html
    fun appEventOccurred(event: ActivityEvent, extras: Map<ActivityEventParamKey, Any>?, average: Double? = null)
    fun setUserProperty(key: UserPropertyKey, value: String)

    // meant for debugging purposes only.
    fun breadcrumb(caller: Any, event: String, extras: Bundle?)
    fun httpRequestEvent(method: String, url: String, reqBody: String?)
    fun httpSuccessEvent(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?)
    fun httpFailEvent(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?)
    fun errorOccurred(error: Throwable)
}
