package com.levibostian.androidblanky.service

import okhttp3.Interceptor
import okhttp3.Response
import org.greenrobot.eventbus.EventBus
import android.net.ConnectivityManager
import com.levibostian.androidblanky.service.error.nonfatal.NoInternetConnectionException
import com.levibostian.androidblanky.service.error.nonfatal.ServerErrorException
import com.levibostian.androidblanky.service.event.LogoutUserEvent

class DefaultErrorHandlerInterceptor(val eventbus: EventBus,
                                     val connectivityManager: ConnectivityManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        if (!isOnline()) throw NoInternetConnectionException()

        val request = chain!!.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val statusCode = response.code()
            when (statusCode) {
                in 500..600 -> throw ServerErrorException()
                401 -> eventbus.post(LogoutUserEvent())
            }
        }

        return chain.proceed(request)
    }

    private fun isOnline(): Boolean {
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

}