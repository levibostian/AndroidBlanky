package com.levibostian.service.error.network

/**
 * When there is a HTTP network error, there could be many different reasons for the error and the app should respond in different ways.
 *
 * If it's a developer error (json parsing problem)...the app should log this error and fix it.
 * If it's a network error...the app should tell the user it's a network error.
 * If it's a user error (http response 400, for example)...the app should tell user the error and help them fix it.
 */
class HttpRequestError private constructor(
    message: String,
    val faultType: FaultType,
    // Optional error which is intended for UI of app to respond to an error in a specific way such as "ConflictResponseError". Check if underlying error is of a type of error and if so, do something special in the app.
    val underlyingError: Throwable?
) : Throwable(message) {

    companion object {
        fun developerError(message: String, underlyingError: Throwable): HttpRequestError = HttpRequestError(message, FaultType.DEVELOPER, underlyingError)

        fun userError(message: String, underlyingError: Throwable?): HttpRequestError = HttpRequestError(message, FaultType.USER, underlyingError)

        fun networkError(message: String): HttpRequestError = HttpRequestError(message, FaultType.NETWORK, null)
    }

    enum class FaultType {
        DEVELOPER,
        USER,
        NETWORK
    }
}
