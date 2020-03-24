package com.levibostian.androidblanky.di

import android.accounts.AccountManager
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import com.levibostian.androidblanky.service.logger.Logger
import com.levibostian.androidblanky.service.util.InstallReferrerProcessor
import javax.inject.Singleton

@Module
class AndroidModule(private val application: Application) {

    @Provides
    fun provideInstallReferrerProcessor(sharedPreferences: SharedPreferences, logger: Logger): InstallReferrerProcessor {
        return InstallReferrerProcessor(sharedPreferences, logger)
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }

    @Provides
    fun provideAccountManager(context: Context): AccountManager {
        return AccountManager.get(context)
    }

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    fun provideConnectivityManager(): ConnectivityManager {
        return application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

}
