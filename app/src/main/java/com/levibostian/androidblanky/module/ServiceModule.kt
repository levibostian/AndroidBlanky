package com.levibostian.androidblanky.module

import android.accounts.AccountManager
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.service.DataDestroyer
import com.levibostian.androidblanky.service.interceptor.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.analytics.AppAnalytics
import com.levibostian.androidblanky.service.dao.ReposDao
import com.levibostian.androidblanky.service.util.ResponseProcessor
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.db.manager.DatabaseManager
import com.levibostian.androidblanky.service.interceptor.MissingDataResponseInterceptor
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit

interface ServiceModule {

    fun provideRetrofit(userManager: UserManager, defaultErrorHandlerInterceptor: DefaultErrorHandlerInterceptor, missingDataResponseInterceptor: MissingDataResponseInterceptor): Retrofit

    fun provideDefaultErrorHandlerInterceptor(connectivityManager: ConnectivityManager, eventBus: EventBus): DefaultErrorHandlerInterceptor

    fun provideMissingDataResponseInterceptor(): MissingDataResponseInterceptor

    fun provideEventbus(): EventBus

    fun provideConnectivityManager(): ConnectivityManager

    fun provideService(retrofit: Retrofit): GitHubService

    fun provideSharedPreferences(): SharedPreferences

    fun provideRxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferencesWrapper

    fun provideResponseProcessor(): ResponseProcessor

    fun provideDataDestroyer(db: Database, accountManager: AccountManager, userManager: UserManager, sharedPreferences: SharedPreferences): DataDestroyer

    fun provideAppAnalytics(): AppAnalytics

}