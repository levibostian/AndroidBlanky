package com.app.service.http

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

/**
 * @param hostname Example: https://foo.com/
 */
abstract class BaseHttpClient(private val hostname: String, private val client: OkHttpClient) {

    fun get(path: String): Result<String> {
        val request = Request.Builder()
            .url("$hostname$path")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return Result.failure(IOException("Unexpected code ${response.code}"))
            return Result.success(response.body!!.string())
        }
    }
}
