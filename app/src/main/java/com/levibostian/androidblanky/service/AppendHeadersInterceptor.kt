package com.levibostian.androidblanky.service

import com.levibostian.androidblanky.service.manager.UserCredsManager
import okhttp3.Interceptor
import okhttp3.Response

class AppendHeadersInterceptor(private val credsManager: UserCredsManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        var request = chain!!.request()

        credsManager.authToken?.let { authToken ->
            request = request
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $authToken")
                    .build()
        }

        return chain.proceed(request)
    }

}