package com.app.service.logger

import android.os.Bundle

class AppLogger(private val loggers: List<Logger>) : Logger {

    override fun setUserId(id: String?) {
        loggers.forEach { it.setUserId(id) }
    }

    override fun appEventOccurred(event: ActivityEvent, extras: Map<ActivityEventParamKey, Any>?, average: Double?) {
        loggers.forEach { it.appEventOccurred(event, extras, average) }
    }

    override fun setUserProperty(key: UserPropertyKey, value: String) {
        loggers.forEach { it.setUserProperty(key, value) }
    }

    override fun breadcrumb(caller: Any, event: String, extras: Bundle?) {
        loggers.forEach { it.breadcrumb(caller, event, extras) }
    }

    override fun httpRequestEvent(method: String, url: String, reqBody: String?) {
        loggers.forEach { it.httpRequestEvent(method, url, reqBody) }
    }

    override fun httpSuccessEvent(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?) {
        loggers.forEach { it.httpSuccessEvent(method, url, code, reqHeaders, resHeaders, resBody) }
    }

    override fun httpFailEvent(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?) {
        loggers.forEach { it.httpFailEvent(method, url, code, reqHeaders, resHeaders, resBody) }
    }

    override fun errorOccurred(error: Throwable) {
        loggers.forEach { it.errorOccurred(error) }
    }
}
