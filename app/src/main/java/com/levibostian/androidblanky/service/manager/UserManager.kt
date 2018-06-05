package com.levibostian.androidblanky.service.manager

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.service.pendingtasks.UpdateFcmTokenPendingTask
import com.levibostian.wendy.service.Wendy

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

            doWorkAfterLoggedIn()
        }

    var fcmPushNotificationToken: String?
        get() = sharedPrefs.getString(SharedPrefersKeys.FCM_PUSH_NOTIFICATION, null)
        @SuppressLint("ApplySharedPref")
        set(value) {
            if (isUserLoggedIn()) {
                doWorkAfterLoggedIn()
            } else {
                sharedPrefs.edit().putString(SharedPrefersKeys.FCM_PUSH_NOTIFICATION, value).commit()
            }
        }

    // Some stuff requires being logged in to perform. Perform that now.
    private fun doWorkAfterLoggedIn() {
        fcmPushNotificationToken?.let {
            Wendy.shared.addTask(UpdateFcmTokenPendingTask(id!!))
            fcmPushNotificationToken = null
        }
    }

}