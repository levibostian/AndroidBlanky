package com.levibostian.service.interceptor

import com.levibostian.service.manager.UserManager
import okhttp3.Interceptor
import okhttp3.Response

class AppendHeadersInterceptor(private val userManager: UserManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var requestBuilder = request.newBuilder()

        requestBuilder = requestBuilder.addHeader("accept-version", "0.1.0")

        userManager.authToken?.let { authToken ->
            requestBuilder = requestBuilder.addHeader("Authorization", "Bearer $authToken")
        }

        return chain.proceed(requestBuilder.build())
    }
}
