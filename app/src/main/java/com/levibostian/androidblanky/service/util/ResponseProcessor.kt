package com.levibostian.androidblanky.service.util

import android.content.Context
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.error.network.NetworkConnectionIssueException
import com.levibostian.androidblanky.service.error.network.NoInternetConnectionException
import com.levibostian.androidblanky.service.error.network.ServerErrorException
import com.levibostian.androidblanky.service.error.network.UnauthorizedException
import com.levibostian.androidblanky.testing.OpenForTesting
import retrofit2.adapter.rxjava2.Result
import timber.log.Timber
import java.io.IOException

@OpenForTesting
class ResponseProcessor(private val context: Context) {

    fun <RESPONSE> process(result: Result<RESPONSE>): Throwable? {
        if (result.response()?.isSuccessful == true) return null
        when (result.error()) {
            is NoInternetConnectionException,
            is UnauthorizedException -> return result.error()
        }

        result.error()?.let { error ->
            if (error is IOException) {
                return NetworkConnectionIssueException(context.getString(R.string.error_network_connection_issue))
            }
            // According to Retrofit's result.error() Javadoc, if the error is not an instance of IOException, it's a programming error and should be looked at. Throw it so we can see it and fix it.
            throw error
        }

        return when (result.response()!!.code()) {
            in 500..600 -> ServerErrorException(context.getString(R.string.error_500_600_response_code))
            401 -> UnauthorizedException(context.getString(R.string.error_401_response_code))
            else -> null
        }
    }

    // During development you should be handling client side all of the ways that a HTTP API call could fail. However, you may forget some. So to handle that, we need to alert ourselves to fix this issue and to return back to our users a human readable message saying that an error we cannot handle it and we are going to fix it.
    fun <RESPONSE> unhandledHttpResult(result: Result<RESPONSE>): String {
        Timber.e(RuntimeException("Fatal HTTP network call. ${result.response()?.toString() ?: "(no HTTP response found)."}"))
        return context.getString(R.string.fatal_network_error_message)
    }

}