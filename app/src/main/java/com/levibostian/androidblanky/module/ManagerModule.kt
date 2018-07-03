package com.levibostian.androidblanky.module

import android.accounts.AccountManager
import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.service.analytics.AppAnalytics
import com.levibostian.androidblanky.service.manager.NotificationChannelManager
import com.levibostian.androidblanky.service.manager.UserManager

interface ManagerModule {

    fun provideUserManager(sharedPrefs: SharedPreferences, accountManager: AccountManager, appAnalytics: AppAnalytics): UserManager

    fun provideNotificationChannelManager(): NotificationChannelManager

    fun provideAccountManager(): AccountManager

}