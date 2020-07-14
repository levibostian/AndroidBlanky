package com.levibostian.service.error.network

import com.levibostian.extensions.getHeadersString
import retrofit2.Response

class UnhandledHttpResultException(val response: Response<*>) : Throwable("Unhandled http result. ${response.raw().request.method}: ${response.raw().request.url} - REQUEST: ${response.raw().request.getHeadersString()}, ${response.raw().request.body?.toString()} - RESPONSE: ${response.headers()}, ${response.body()}")
