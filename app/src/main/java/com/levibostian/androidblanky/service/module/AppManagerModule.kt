package com.levibostian.androidblanky.service.module

import android.content.Context
import android.content.SharedPreferences
import com.levibostian.androidblanky.module.ManagerModule

import com.levibostian.androidblanky.service.manager.UserCredsManager
import com.levibostian.androidblanky.service.manager.UserManager

import dagger.Module
import dagger.Provides

@Module class AppManagerModule(private val context: Context): ManagerModule {

    @Provides override fun provideUserCredsManager(sharedPrefs: SharedPreferences): UserCredsManager {
        return UserCredsManager(sharedPrefs)
    }

    @Provides override fun provideUserManager(sharedPrefs: SharedPreferences): UserManager {
        return UserManager(context, sharedPrefs)
    }

}

