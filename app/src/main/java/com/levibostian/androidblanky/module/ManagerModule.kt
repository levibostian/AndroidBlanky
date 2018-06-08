package com.levibostian.androidblanky.module

import android.accounts.AccountManager
import android.content.SharedPreferences
import com.levibostian.androidblanky.service.manager.NotificationChannelManager
import com.levibostian.androidblanky.service.manager.UserManager

interface ManagerModule {

    fun provideUserManager(sharedPrefs: SharedPreferences, accountManager: AccountManager): UserManager

    fun provideNotificationChannelManager(): NotificationChannelManager

    fun provideAccountManager(): AccountManager

}