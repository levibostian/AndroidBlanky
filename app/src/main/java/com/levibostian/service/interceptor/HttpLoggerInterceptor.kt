package com.levibostian.service.interceptor

import com.levibostian.extensions.getBodyCopy
import com.levibostian.extensions.getHeadersString
import com.levibostian.service.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response

class HttpLoggerInterceptor(private val logger: Logger) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url.toString()
        val method = request.method

        logger.httpRequestEvent(method, url, request.body?.toString())

        val response = chain.proceed(request)

        // We only care about the body if the response is not a successful one.
        if (response.isSuccessful) {
            logger.httpSuccessEvent(
                method = method,
                url = url,
                code = response.code,
                reqHeaders = request.getHeadersString(),
                resHeaders = response.getHeadersString(),
                resBody = response.getBodyCopy()
            )
        } else {
            logger.httpFailEvent(
                method = method,
                url = url,
                code = response.code,
                reqHeaders = request.getHeadersString(),
                resHeaders = response.getHeadersString(),
                resBody = response.getBodyCopy()
            )
        }

        return response
    }
}
