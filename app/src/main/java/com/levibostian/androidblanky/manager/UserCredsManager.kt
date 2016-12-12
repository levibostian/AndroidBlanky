package com.levibostian.androidblanky.manager

import android.content.Context
import com.curiosityio.androidboilerplate.manager.SharedPreferencesManager
import com.levibostian.androidblanky.R

open class UserCredsManager(val context: Context) {

    fun getAuthToken(): String? {
        return SharedPreferencesManager.getString(context, context.getString(R.string.preferences_auth_token))
    }

}