package com.levibostian.service.api

import android.content.Context
import com.levibostian.R
import com.levibostian.service.error.network.HttpRequestError
import com.levibostian.service.error.network.NoInternetConnectionException
import com.levibostian.service.error.network.UnhandledHttpResultException
import com.levibostian.service.logger.Logger
import io.reactivex.Single
import java.io.IOException
import javax.inject.Inject

/**
 * Class that encapsulates http requests while also handling problems that can occur for each.
 *
 * The point of this class is to contain a function for all endpoints of an API. Each function does all of the deserialization and extra error handling. Let's say 1 endpoint can return a 400 error while others do not. The function for that certain endpoint will handle the 400 response for you.
 *
 * Each function returns a Kotlin Result type. This means that Rx onError will not be called. Either the Http function was successful (200-300 response with a deserialized VO object returned) or there was an error. The Result.failure exception will have a human readable error message that you can display to the user.
 *
 * If there is an error, this class will handle it. If it's a network error, it will simply give you that message so you can display that to the user. If it's a user error, the error will be optionally parsed from the response to be shown for the user and the UI can handle the error if it wants to. If it's a developer error, the error will be logged to notify the developer team to fix it.
 */
abstract class Api constructor(private val context: Context,
                               private val logger: Logger) {

    /**
     * Performs the bulk of the network call for you. Optional help is optional (JSON parsing or handling of other status codes).
     *
     * The flow of a network call:
     * - Perform http call
     *   - If error, the http call did not actually perform. No status got returned. It could be no Internet connection or a bad Internet connection.
     *     - When an error happens, see if it's a network issue. If it is, return that result to the user to display to them.
     *     - If it's not a network issue, it's a developer issue. Log the exception and return.
     *   - If no error, the http call did perform. A response came back from the server.
     *     - Check the status code. If it's a code that could happen with any call (500, 401), then return that error to the user.
     *     - The caller then has the chance to see the response and process it if it wants to. Some endpoints could return a 400, for example, while others might not.
     *     - If the response is a 200-300 response, it is successful and the caller can help transform the response body into an object for you and then this function returns that result.
     *
     * This function needs to do the common things that all network calls need to do. Handle the many scenarios. When there is a problem, determine what the problem is act on it. If it's a developer error, for example, it will log that error for you. If it's any other type of error, it will not log it. The function will make sure the error is returned back.
     *
     * @param extraErrorHandling If an endpoint returns back other status codes this default API does not handle for you. Handle what you can with this closure. Return null if you don't catch it and an error will be logged to the developer to notify you to fix the app.
     * @param extraSuccessHandling If a response from the API is not a 200..300 response but it's still successful, return a result from this function and you will get back a [Result.success] result with what you return.
     */
    fun <HttpResponse: Any> request(target: Single<retrofit2.adapter.rxjava2.Result<HttpResponse>>, extraErrorHandling: ((ProcessedResponse) -> HttpRequestError?)? = null, extraSuccessHandling: ((ProcessedResponse) -> HttpResponse?)? = null): Single<Result<HttpResponse>> {
        return target.map { result ->
            if (result.isError) { // http call failed. There is no response.
                Result.failure(handleRequestFailure(result.error()!!))
            } else {
                if (result.response()!!.isSuccessful) { // 200-300 status code
                    Result.success(result.response()!!.body()!!) // the only time we return a Result.success result.
                } else {
                    val processedResponse = ProcessedResponse(result.response()!!.raw().request.url.toString(), result.response()!!.raw().request.method, result.response()!!.code(), result.response()!!.errorBody()!!.string())

                    if (extraSuccessHandling != null) {
                        val successfulResult = extraSuccessHandling(processedResponse)

                        if (successfulResult != null) return@map Result.success(successfulResult)
                    }

                    val httpRequestError = handleUnsuccessfulStatusCode(processedResponse)
                            ?: extraErrorHandling?.invoke(processedResponse)
                            ?: HttpRequestError.developerError(context.getString(R.string.unhandled_http_response_error_message), UnhandledHttpResultException(result.response()!!))

                    if (httpRequestError.faultType == HttpRequestError.FaultType.DEVELOPER) {
                        logger.errorOccurred(httpRequestError.underlyingError!!)
                    }

                    Result.failure(httpRequestError)
                }
            }
        }
    }

    fun handleRequestFailure(failure: Throwable): HttpRequestError {
        return when (failure) {
            // No Internet
            is NoInternetConnectionException -> HttpRequestError.networkError(context.getString(R.string.no_internet_connection_error_message))
            // Bad network connection
            is IOException -> HttpRequestError.networkError(context.getString(R.string.error_network_connection_issue))
            // Some other error. Have the developer check it out. According to Retrofit's result.error() Javadoc, if the error is not an instance of IOException, it's a programming error and should be looked at. Throw it so we can see it and fix it.
            else -> {
                logger.errorOccurred(failure)

                HttpRequestError.developerError(context.getString(R.string.fatal_network_error_message), failure)
            }
        }
    }

    abstract fun handleUnsuccessfulStatusCode(processedResponse: ProcessedResponse): HttpRequestError?

}