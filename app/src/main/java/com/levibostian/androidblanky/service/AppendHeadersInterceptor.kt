package com.levibostian.androidblanky.service

import com.levibostian.androidblanky.viewmodel.UserCredsManager
import okhttp3.Interceptor
import okhttp3.Response

class AppendHeadersInterceptor(val credsManager: UserCredsManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val authToken = credsManager.authToken
        var request = chain!!.request()

        if (authToken != null) {
            request = request
                    .newBuilder()
                    .addHeader("Access-Token", credsManager.authToken)
                    .build()
        }

        return chain.proceed(request)
    }

}