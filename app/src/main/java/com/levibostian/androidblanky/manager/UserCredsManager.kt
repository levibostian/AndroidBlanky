package com.levibostian.androidblanky.manager

import android.content.Context
import android.content.SharedPreferences
import com.levibostian.androidblanky.R

open class UserCredsManager(val sharedPrefs: SharedPreferences) {

    var authToken: String?
        get() = sharedPrefs.getString("auth_token", null)
        set(value) {
            sharedPrefs.edit().putString("auth_token", value).commit()
        }

}