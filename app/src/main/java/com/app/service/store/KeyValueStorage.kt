package com.app.service.store

import android.content.SharedPreferences
import androidx.core.content.edit
import com.app.extensions.*
import java.util.*
import javax.inject.Inject

enum class KeyValueStorageKey {
    LOGGED_IN_USER_ID,
    LOGGED_IN_USER_AUTH_TOKEN;
}

class KeyValueStorage @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun bool(key: KeyValueStorageKey): Boolean = sharedPreferences.getBoolean(key.name, false)

    fun setBool(value: Boolean, key: KeyValueStorageKey) = sharedPreferences.edit(commit = true) {
        putBoolean(key.name, value)
    }

    fun integer(key: KeyValueStorageKey): Int? = sharedPreferences.getInt(key.name, null)

    fun setInt(value: Int?, key: KeyValueStorageKey) = sharedPreferences.edit(commit = true) {
        putInt(key.name, value)
    }

    fun long(key: KeyValueStorageKey): Long? = sharedPreferences.getLong(key.name, null)

    fun setLong(value: Long?, key: KeyValueStorageKey) = sharedPreferences.edit(commit = true) {
        putLong(key.name, value)
    }

    fun string(key: KeyValueStorageKey): String? = sharedPreferences.getString(key.name, null)

    fun setString(value: String?, key: KeyValueStorageKey) = sharedPreferences.edit(commit = true) {
        putString(key.name, value)
    }

    fun date(key: KeyValueStorageKey): Date? = sharedPreferences.getDate(key.name, null)

    fun setDate(value: Date?, key: KeyValueStorageKey) = sharedPreferences.edit(commit = true) {
        putDate(key.name, value)
    }

    fun deleteAll() {
        sharedPreferences.edit(commit = true) {
            clear()
        }
    }
}
