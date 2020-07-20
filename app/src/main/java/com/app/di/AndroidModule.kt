package com.app.di

import android.accounts.AccountManager
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.preference.PreferenceManager
import com.app.service.ResetAppRunner
import com.app.view.ui.MainApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val application: MainApplication) {

    @Provides
    fun provideAppResetRunner(): ResetAppRunner {
        return application
    }

    @Provides
    fun provideApplication(): MainApplication {
        return application
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
