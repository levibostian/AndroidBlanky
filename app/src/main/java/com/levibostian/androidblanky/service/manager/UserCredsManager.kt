package com.levibostian.androidblanky.service.manager

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.levibostian.androidblanky.service.model.SharedPrefersKeys

class UserCredsManager(private val sharedPrefs: SharedPreferences) {

    var authToken: String?
        get() = sharedPrefs.getString(SharedPrefersKeys.USER_AUTH_TOKEN, null)
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPrefs.edit().putString(SharedPrefersKeys.USER_AUTH_TOKEN, value).commit()
        }

}