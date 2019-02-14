package com.levibostian.androidblanky.service.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import android.net.ConnectivityManager
import com.levibostian.androidblanky.R
import com.levibostian.androidblanky.service.error.network.NoInternetConnectionException
import com.levibostian.androidblanky.service.error.network.UnauthorizedException
import com.levibostian.androidblanky.service.event.LogoutUserEvent
import com.levibostian.androidblanky.service.manager.UserManager

class DefaultErrorHandlerInterceptor(private val context: Context,
                                     private val eventbus: EventBus,
                                     private val connectivityManager: ConnectivityManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        if (!isOnline()) throw NoInternetConnectionException(context.getString(R.string.no_internet_connection_error_message))

        val request = chain!!.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val statusCode = response.code()
            when (statusCode) {
                401 -> {
                    eventbus.post(LogoutUserEvent(true))
                    throw UnauthorizedException(context.getString(R.string.error_401_response_code))
                }
            }
        }

        return response
    }

    private fun isOnline(): Boolean {
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

}