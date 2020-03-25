package com.levibostian.service.manager

import android.accounts.Account
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.levibostian.service.auth.AccountAuthenticator
import com.levibostian.service.logger.Logger
import com.levibostian.service.model.SharedPrefersKeys
import com.levibostian.service.pendingtasks.UpdateFcmTokenPendingTask
import com.levibostian.wendy.service.Wendy
import javax.inject.Inject

class UserManager @Inject constructor(private val sharedPrefs: SharedPreferences,
                                      private val deviceAccountManager: DeviceAccountManager) {

    fun isUserLoggedIn(): Boolean {
        return (id != null) && (authToken != null) && (email != null)
    }

    // If a user goes into the device's settings > accounts and delete the account for this app and then relaunches this app, the id and email will not be null but the token will since that is stored in the device's account manager. So, check for that case here.
    fun doesUserAccountNeedUnlocked(): Boolean {
        return (id != null) && (email != null) && (authToken == null)
    }

    fun logout() {
        id = null
        email = null
        deviceAccountManager.logout()
    }

    val authToken: String?
        get() = deviceAccountManager.accessToken

    var id: String?
        get() = sharedPrefs.getString(SharedPrefersKeys.USER_ID, null)
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPrefs.edit {
                putString(SharedPrefersKeys.USER_ID, value)
            }

            doWorkAfterLoggedIn()
        }

    var email: String?
        get() = sharedPrefs.getString(SharedPrefersKeys.USER_EMAIL, null)
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPrefs.edit {
                putString(SharedPrefersKeys.USER_EMAIL, value)
            }
        }

    var fcmPushNotificationToken: String?
        get() = sharedPrefs.getString(SharedPrefersKeys.FCM_PUSH_NOTIFICATION, null)
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPrefs.edit(commit = true) {
                putString(SharedPrefersKeys.FCM_PUSH_NOTIFICATION, value)
            }

            if (isUserLoggedIn()) doWorkAfterLoggedIn()
        }

    // Some stuff requires being logged in to perform. Perform that now.
    private fun doWorkAfterLoggedIn() {
        fcmPushNotificationToken?.let {
            Wendy.shared.addTask(UpdateFcmTokenPendingTask(id!!))
            fcmPushNotificationToken = null
        }
    }

}