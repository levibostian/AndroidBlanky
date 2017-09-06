package com.levibostian.androidblanky.viewmodel.module

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.levibostian.androidblanky.viewmodel.UserCredsManager

import dagger.Module
import dagger.Provides

@Module class ManagerModule(private val context: Context) {

    @Provides fun provideUserCredsManager(sharedPrefs: SharedPreferences): UserCredsManager {
        return UserCredsManager(sharedPrefs)
    }

    @Provides fun provideSharedPrefs(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

}

