package com.levibostian.androidblanky.service.model

import android.content.Context
import com.levibostian.androidblanky.R

// It's best to have constants across the app instead of some loose strings. This file is intended to be the 1 place that all shared preferences strings are obtained.
object SharedPrefersKeys {

    const val USER_AUTH_TOKEN: String = "USER_AUTH_TOKEN"
    const val USER_ID: String = "USER_ID"
    const val FIRST_APP_LAUNCH: String = "FIRST_APP_LAUNCH"
    const val FCM_PUSH_NOTIFICATION: String = "FCM_PUSH_NOTIFICATION"

    // Some of the preferences keys must be stored in a string reference file (mostly because string is referred to in a XML file) so we must obtain it via context.
    fun enableAnalytics(context: Context): String {
        return context.getString(R.string.preferences_enable_analytics)
    }

}