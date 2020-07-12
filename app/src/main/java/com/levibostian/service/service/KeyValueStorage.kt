package com.levibostian.service.service

import android.content.SharedPreferences
import androidx.core.content.edit
import com.levibostian.extensions.*
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

enum class KeyValueStorageKey {
    LOGGED_IN_USER_ID,
    LOGGED_IN_USER_AUTH_TOKEN,
    ENABLE_ANALYTICS;
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

    // Does not emit when value is nil or empty. it's recommended you check if the key has a value first and then observe for changes.
    fun observeString(key: KeyValueStorageKey): Observable<String> = sharedPreferences.observeString(key.name)
            .filter { it.isNotEmpty() }

    fun deleteAll() {
        sharedPreferences.edit(commit = true) {
            clear()
        }
    }

}