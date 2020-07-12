package com.levibostian.service.logger

import android.app.Activity
import android.os.Bundle

/**
 * [Logger] intended for development purposes. It's meant to take many of the functions and turn them into strings that can be shown to the developer.
 */
abstract class DebugLogger: Logger {

    abstract fun logDebug(message: String, extras: Bundle?)
    abstract fun logError(error: Throwable)

    override fun breadcrumb(caller: Any, event: String, extras: Bundle?) {
        logDebug("$event (from: ${caller::class.java.simpleName})", extras)
    }

    override fun httpRequestEvent(method: String, url: String, reqBody: String?) {
        logDebug("Http request-- method: $method, url: $url, req body: ${reqBody ?: "(none)"}", null)
    }

    override fun httpSuccessEvent(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?) {
        logDebug("Http response success-- method: $method, url: $url, code: $code, res body: ${resBody ?: "(none)"}", null)
    }

    override fun httpFailEvent(method: String, url: String, code: Int, reqHeaders: String?, resHeaders: String?, resBody: String?) {
        logDebug("Http Response Failed! method: method: $method, url: $url, code: $code, req headers: ${reqHeaders ?: "(none)"}, res headers: ${resHeaders ?: "(none)"}, res body: ${resBody ?: "(none)"}", null)
    }

    override fun errorOccurred(error: Throwable) {
        logError(error)
    }
}