package com.levibostian.androidblanky.extensions

import okhttp3.Headers
import okhttp3.Request
import okhttp3.Response

fun Response.getBodyCopy(): String? {
    return if (body() != null) {
        val responseBodyCopy = peekBody(java.lang.Long.MAX_VALUE) // We can only call response.body().string() *once* from Okhttp because of it trying to be conservative to memory. So, we must create a copy of the body. https://github.com/square/okhttp/issues/1240#issuecomment-330813274
        val responseBody = responseBodyCopy.string()

        responseBody
    } else {
        null
    }
}

fun Request.getHeadersString(): String? {
    return headers().getString()
}

fun Headers.getString(): String? {
    var i = 0
    val count = this.size()
    val headerStrings: ArrayList<String> = arrayListOf()
    while (i < count) {
        val name = this.name(i)
        headerStrings.add("$name: ${this.value(i)}")
        i++
    }

    return if (headerStrings.isEmpty()) null else headerStrings.joinToString(separator = ", ")
}

fun Response.getHeadersString(): String? {
    return headers().getString()
}