package com.levibostian.androidblanky.service.module

import android.accounts.AccountManager
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.module.ManagerModule
import com.levibostian.androidblanky.service.analytics.AppAnalytics
import com.levibostian.androidblanky.service.manager.NotificationChannelManager

import com.levibostian.androidblanky.service.manager.UserManager

import dagger.Module
import dagger.Provides

@Module class AppManagerModule(private val context: Context): ManagerModule {

    @Provides override fun provideUserManager(sharedPrefs: SharedPreferences, accountManager: AccountManager, appAnalytics: AppAnalytics): UserManager {
        return UserManager(context, sharedPrefs, accountManager, appAnalytics)
    }

    @Provides override fun provideNotificationChannelManager(): NotificationChannelManager = NotificationChannelManager(context)

    @Provides override fun provideAccountManager(): AccountManager = AccountManager.get(context)

}

