package com.levibostian.androidblanky.module

import android.content.Context
import android.content.SharedPreferences

import com.levibostian.androidblanky.manager.UserCredsManager
import com.levibostian.androidblanky.service.GitHubService

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module class ManagerModule(private val context: Context) {

    @Provides fun provideUserCredsManager(): UserCredsManager {
        return UserCredsManager(context)
    }

}

