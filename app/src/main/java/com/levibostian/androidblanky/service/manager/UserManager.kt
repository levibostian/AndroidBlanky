package com.levibostian.androidblanky.service.manager

import android.accounts.Account
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.service.analytics.AppAnalytics
import com.levibostian.androidblanky.service.auth.AccountAuthenticator
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.service.pendingtasks.UpdateFcmTokenPendingTask
import com.levibostian.wendy.service.Wendy

class UserManager(private val context: Context,
                  private val sharedPrefs: SharedPreferences,
                  private val accountManager: AccountManager,
                  private val analytics: AppAnalytics) {

    fun isUserLoggedIn(): Boolean {
        return (id != null) && (getAccount() != null) && (authToken != null) && (email != null)
    }

    fun doesUserAccountNeedUnlocked(): Boolean {
        return (id != null) && (email != null) && (getAccount() == null)
    }

    fun getAccount(): Account? {
        return accountManager.getAccountsByType(AccountAuthenticator.ACCOUNT_TYPE).getOrNull(0)
    }

    fun logout() {
        id = null
        authToken = null
        email = null
        analytics.setUserId(null)
    }

    var authToken: String? = null
        get() {
            getAccount()?.let { account ->
                return accountManager.peekAuthToken(account, AccountAuthenticator.ACCOUNT_TYPE)
            }
            return null
        }

    var id: String?
        get() = sharedPrefs.getString(SharedPrefersKeys.USER_ID, null)
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPrefs.edit().putString(SharedPrefersKeys.USER_ID, value).commit()
            analytics.setUserId(value)

            doWorkAfterLoggedIn()
        }

    var email: String?
        get() = sharedPrefs.getString(SharedPrefersKeys.USER_EMAIL, null)
        @SuppressLint("ApplySharedPref")
        set(value) { sharedPrefs.edit().putString(SharedPrefersKeys.USER_EMAIL, value).commit() }

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