package com.app.service.manager

import com.app.service.KeyValueStorage
import com.app.service.KeyValueStorageKey
import javax.inject.Inject

/**
 * Used to store data about the user of the app. If your app allows people to login with email and password, for example, we use this class to store information about the user such as name or ID number.
 */
class UserManager @Inject constructor(private val keyValueStorage: KeyValueStorage) {

    val isUserLoggedIn: Boolean
        get() = id != null && authToken != null

    fun logout() {
        id = null
        authToken = null
    }

    var id: Int?
        get() = keyValueStorage.integer(KeyValueStorageKey.LOGGED_IN_USER_ID)
        set(value) {
            keyValueStorage.setInt(value, KeyValueStorageKey.LOGGED_IN_USER_ID)
        }

    var authToken: String?
        get() = keyValueStorage.string(KeyValueStorageKey.LOGGED_IN_USER_AUTH_TOKEN)
        set(value) {
            keyValueStorage.setString(value, KeyValueStorageKey.LOGGED_IN_USER_AUTH_TOKEN)
        }
}
