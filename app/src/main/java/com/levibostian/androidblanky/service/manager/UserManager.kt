package com.levibostian.androidblanky.service.manager

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.levibostian.androidblanky.service.model.SharedPrefersKeys

class UserManager(private val sharedPrefs: SharedPreferences) {

    var id: String?
        get() = sharedPrefs.getString(SharedPrefersKeys.userIdKey, null)
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPrefs.edit().putString(SharedPrefersKeys.userIdKey, value).commit()
        }

}