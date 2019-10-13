package com.levibostian.androidblanky.module

import android.accounts.AccountManager
import android.content.Context
import android.net.ConnectivityManager
import androidx.preference.PreferenceManager
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object AndroidModule {

    fun get(): Module {
        return module {
            factory { AccountManager.get(androidContext()) }
            factory { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
            factory { androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
        }
    }

}