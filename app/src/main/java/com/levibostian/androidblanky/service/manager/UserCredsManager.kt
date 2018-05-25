package com.levibostian.androidblanky.service.manager

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.levibostian.androidblanky.service.model.SharedPrefersKeys

class UserCredsManager(private val sharedPrefs: SharedPreferences) {

    var authToken: String?
        get() = sharedPrefs.getString(SharedPrefersKeys.userAuthTokenKey, null)
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPrefs.edit().putString(SharedPrefersKeys.userAuthTokenKey, value).commit()
        }

}