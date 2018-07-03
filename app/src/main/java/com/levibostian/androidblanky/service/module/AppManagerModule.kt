package com.levibostian.androidblanky.service.module

import android.accounts.AccountManager
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.module.ManagerModule
import com.levibostian.androidblanky.service.analytics.AppAnalytics
import com.levibostian.androidblanky.service.manager.NotificationChannelManager

import com.levibostian.androidblanky.service.manager.UserManager

import dagger.Module
import dagger.Provides

@Module class AppManagerModule: ManagerModule {

    @Provides override fun provideUserManager(application: Application, sharedPrefs: SharedPreferences, accountManager: AccountManager, appAnalytics: AppAnalytics): UserManager {
        return UserManager(application, sharedPrefs, accountManager, appAnalytics)
    }

    @Provides override fun provideNotificationChannelManager(application: Application): NotificationChannelManager = NotificationChannelManager(application)

    @Provides override fun provideAccountManager(application: Application): AccountManager = AccountManager.get(application)

}

