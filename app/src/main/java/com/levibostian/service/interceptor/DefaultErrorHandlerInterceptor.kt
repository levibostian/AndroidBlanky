package com.levibostian.service.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import com.levibostian.R
import com.levibostian.service.error.network.NoInternetConnectionException
import com.levibostian.service.error.network.UnauthorizedException
import com.levibostian.service.event.LogoutUserEvent
import com.levibostian.service.util.ConnectivityUtil
import javax.inject.Inject

class DefaultErrorHandlerInterceptor @Inject constructor(private val context: Context,
                                                         private val eventbus: EventBus,
                                                         private val connectivityUtil: ConnectivityUtil) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline()) throw NoInternetConnectionException(context.getString(R.string.no_internet_connection_error_message))

        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            when (response.code) {
                401 -> {
                    eventbus.post(LogoutUserEvent())
                    throw UnauthorizedException(context.getString(R.string.error_401_response_code))
                }
            }
        }

        return response
    }

    private fun isOnline(): Boolean = connectivityUtil.isNetworkAvailable()

}