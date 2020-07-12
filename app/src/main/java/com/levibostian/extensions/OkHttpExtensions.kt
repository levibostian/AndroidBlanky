package com.levibostian.extensions

import com.levibostian.service.json.JsonAdapter
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

fun Response.getBodyCopy(): String? {
    return if (body != null) {
        val responseBodyCopy = peekBody(java.lang.Long.MAX_VALUE) // We can only call response.body().string() *once* from Okhttp because of it trying to be conservative to memory. So, we must create a copy of the body. https://github.com/square/okhttp/issues/1240#issuecomment-330813274
        val responseBody = responseBodyCopy.string()

        responseBody
    } else {
        null
    }
}

fun Request.getHeadersString(): String? {
    return headers.getString()
}

fun Headers.getString(): String? {
    var i = 0
    val count = this.size
    val headerStrings: ArrayList<String> = arrayListOf()
    while (i < count) {
        val name = this.name(i)
        headerStrings.add("$name: ${this.value(i)}")
        i++
    }

    return if (headerStrings.isEmpty()) null else headerStrings.joinToString(separator = ", ")
}

fun Response.getHeadersString(): String? {
    return headers.getString()
}

object Response {
    fun <HttpResponseError, T> error(code: Int, body: T): retrofit2.Response<HttpResponseError> {
        return retrofit2.Response.error(code, JsonAdapter.toJson(body as Any).toResponseBody("application/json".toMediaType()))
    }
}