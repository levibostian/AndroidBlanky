package com.levibostian.androidblanky.service.interceptor

import com.levibostian.androidblanky.service.manager.UserManager
import okhttp3.Interceptor
import okhttp3.Response

class AppendHeadersInterceptor(private val userManager: UserManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        var request = chain!!.request()

        userManager.authToken?.let { authToken ->
            request = request
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $authToken")
                    .build()
        }

        return chain.proceed(request)
    }

}