package com.levibostian.androidblanky.module

import android.content.SharedPreferences
import com.levibostian.androidblanky.service.manager.UserCredsManager
import com.levibostian.androidblanky.service.manager.UserManager

interface ManagerModule {

    fun provideUserCredsManager(sharedPrefs: SharedPreferences): UserCredsManager

    fun provideUserManager(sharedPrefs: SharedPreferences): UserManager

}