package com.levibostian.androidblanky.service.json

import com.squareup.moshi.Moshi

/**
 * Abstract way to deserialize JSON strings without thinking about the library used.
 */
class JsonAdapter(private val moshi: Moshi) {

    @Suppress("UNCHECKED_CAST")
    fun <T> fromJson(json: String, clazz: Class<T>): T {
        val jsonAdapter = moshi.adapter<T>(clazz)

        return jsonAdapter.fromJson(json) as T
    }

}