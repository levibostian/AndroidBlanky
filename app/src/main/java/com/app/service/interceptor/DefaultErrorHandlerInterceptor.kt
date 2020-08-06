package com.app.service.interceptor

import android.content.Context
import com.app.R
import com.app.service.error.network.NoInternetConnectionException
import com.app.service.util.ConnectivityUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class DefaultErrorHandlerInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val connectivityUtil: ConnectivityUtil
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline()) throw NoInternetConnectionException(context.getString(R.string.no_internet_connection_error_message))

        val request = chain.request()

        return chain.proceed(request)
    }

    private fun isOnline(): Boolean = connectivityUtil.isNetworkAvailable()
}
