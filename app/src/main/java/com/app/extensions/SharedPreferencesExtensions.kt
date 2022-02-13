package com.app.extensions

import android.content.SharedPreferences
import java.util.*

/**
 * Date
 */
fun SharedPreferences.getDate(key: String, default: Date?): Date? {
    val time = getLong(key, null)
    return if (time == null) default
    else Date(time)
}

fun SharedPreferences.Editor.putDate(key: String, value: Date?): SharedPreferences.Editor {
    putLong(key, value?.time)
    return this
}

/**
 * Long
 */
fun SharedPreferences.Editor.putLong(key: String, value: Long?): SharedPreferences.Editor {
    putLong(key, value ?: Long.MIN_VALUE)
    return this
}

fun SharedPreferences.getLong(key: String, default: Long?): Long? {
    val prefsValue = getLong(key, Long.MIN_VALUE)

    return if (prefsValue == Long.MIN_VALUE) default else prefsValue
}

/**
 * Int
 */
fun SharedPreferences.Editor.putInt(key: String, value: Int?): SharedPreferences.Editor {
    putInt(key, value ?: Int.MIN_VALUE)
    return this
}

fun SharedPreferences.getInt(key: String, default: Int?): Int? {
    val prefsValue = getInt(key, Int.MIN_VALUE)

    return if (prefsValue == Int.MIN_VALUE) default else prefsValue
}
