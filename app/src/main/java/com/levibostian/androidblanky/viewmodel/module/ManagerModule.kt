package com.levibostian.androidblanky.viewmodel.module

import android.content.Context
import android.content.SharedPreferences

import com.levibostian.androidblanky.service.manager.UserCredsManager
import com.levibostian.androidblanky.service.manager.UserManager

import dagger.Module
import dagger.Provides

@Module class ManagerModule(private val context: Context) {

    @Provides fun provideUserCredsManager(sharedPrefs: SharedPreferences): UserCredsManager {
        return UserCredsManager(sharedPrefs)
    }

    @Provides fun provideUserManager(sharedPrefs: SharedPreferences): UserManager {
        return UserManager(sharedPrefs)
    }

}

