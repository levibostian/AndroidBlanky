package com.levibostian.service.interceptor

import android.content.Context
import com.levibostian.R
import com.levibostian.service.error.network.NoInternetConnectionException
import com.levibostian.service.util.ConnectivityUtil
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class DefaultErrorHandlerInterceptor @Inject constructor(
    private val context: Context,
    private val connectivityUtil: ConnectivityUtil
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline()) throw NoInternetConnectionException(context.getString(R.string.no_internet_connection_error_message))

        val request = chain.request()

        return chain.proceed(request)
    }

    private fun isOnline(): Boolean = connectivityUtil.isNetworkAvailable()
}
