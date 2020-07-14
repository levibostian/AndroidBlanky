package com.levibostian.util

object RandomStringUtil {

    private val ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase()

    fun random(count: Int): String {
        @Suppress("NAME_SHADOWING")
        var count = count
        val builder = StringBuilder()

        while (count-- != 0) {
            val character = (Math.random() * ALPHA_NUMERIC_STRING.length).toInt()
            builder.append(ALPHA_NUMERIC_STRING[character])
        }

        return builder.toString()
    }
}
