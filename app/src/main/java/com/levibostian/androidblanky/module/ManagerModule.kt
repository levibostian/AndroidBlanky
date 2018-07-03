package com.levibostian.androidblanky.module

import android.accounts.AccountManager
import android.app.Application
import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.service.analytics.AppAnalytics
import com.levibostian.androidblanky.service.manager.NotificationChannelManager
import com.levibostian.androidblanky.service.manager.UserManager

interface ManagerModule {

    fun provideUserManager(application: Application, sharedPrefs: SharedPreferences, accountManager: AccountManager, appAnalytics: AppAnalytics): UserManager

    fun provideNotificationChannelManager(application: Application): NotificationChannelManager

    fun provideAccountManager(application: Application): AccountManager

}