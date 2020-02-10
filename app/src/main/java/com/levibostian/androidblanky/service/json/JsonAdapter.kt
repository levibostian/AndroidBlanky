package com.levibostian.androidblanky.service.json

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

/**
 * Abstract way to deserialize JSON strings without thinking about the library used.
 */
class JsonAdapter @Inject constructor(private val moshi: Moshi) {

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> fromJson(json: String, clazz: Class<T>): T {
        val jsonAdapter = moshi.adapter<T>(clazz)

        return jsonAdapter.fromJson(json) as T
    }

    fun <T: Any> toJson(data: T): String {
        val jsonAdapter = moshi.adapter<T>(data::class.java)

        return jsonAdapter.toJson(data)
    }

}