package com.levibostian.androidblanky.service.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.service.model.SharedPrefersKeys

class UserManager(private val context: Context, private val sharedPrefs: SharedPreferences) {

    fun isUserLoggedIn(): Boolean = id != null

    fun logout() {
        id = null
    }

    var id: String?
        get() = sharedPrefs.getString(SharedPrefersKeys.USER_ID, null)
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPrefs.edit().putString(SharedPrefersKeys.USER_ID, value).commit()
            FirebaseAnalytics.getInstance(context).setUserId(id)
        }

}