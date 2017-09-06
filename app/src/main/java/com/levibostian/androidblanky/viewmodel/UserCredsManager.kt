package com.levibostian.androidblanky.viewmodel

import android.content.SharedPreferences

open class UserCredsManager(val sharedPrefs: SharedPreferences) {

    var authToken: String?
        get() = sharedPrefs.getString("auth_token", null)
        set(value) {
            sharedPrefs.edit().putString("auth_token", value).commit()
        }

}