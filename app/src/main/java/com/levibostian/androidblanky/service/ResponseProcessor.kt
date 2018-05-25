package com.levibostian.androidblanky.service

import android.content.Context
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.error.NetworkConnectionIssueException
import com.levibostian.androidblanky.service.error.NoInternetConnectionException
import com.levibostian.androidblanky.service.error.ServerErrorException
import retrofit2.adapter.rxjava2.Result
import java.io.IOException

class ResponseProcessor(private val context: Context) {

    fun <RESPONSE> process(result: Result<RESPONSE>): Throwable? {
        if (result.response()?.isSuccessful == true) return null
        if (result.error() is NoInternetConnectionException) return result.error()

        result.error()?.let { error ->
            if (error is IOException) {
                return NetworkConnectionIssueException(context.getString(R.string.error_network_connection_issue))
            }
            // According to Retrofit's result.error() Javadoc, if the error is not an instance of IOException, it's a programming error and should be looked at. Throw it so we can see it and fix it.
            throw error
        }

        return when (result.response()!!.code()) {
            in 500..600 -> {
                ServerErrorException(context.getString(R.string.error_500_600_response_code))
            }
            else -> null
        }
    }
}