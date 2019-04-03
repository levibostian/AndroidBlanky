package com.levibostian.androidblanky.service.interceptor

import com.levibostian.androidblanky.extensions.getBodyCopy
import com.levibostian.androidblanky.extensions.getHeadersString
import com.levibostian.androidblanky.service.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response

class HttpLoggerInterceptor(private val logger: Logger): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url().toString()
        val method = request.method()

        logger.httpRequest(
                method = method,
                url = url)

        val response = chain.proceed(request)

        // We only care about the body if the response is not a successful one.
        if (response.isSuccessful) {
            logger.httpSuccess(
                    method = method,
                    url = url)
        } else {
            logger.httpFail(
                    method = method,
                    url = url,
                    code = response.code(),
                    reqHeaders = request.getHeadersString(),
                    resHeaders = response.getHeadersString(),
                    resBody = response.getBodyCopy()
            )
        }

        return response
    }

}