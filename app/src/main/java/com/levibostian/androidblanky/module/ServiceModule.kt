package com.levibostian.androidblanky.module

import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.levibostian.androidblanky.service.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.ResponseProcessor
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.manager.UserCredsManager
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit

interface ServiceModule {

    fun provideRetrofit(credsManager: UserCredsManager, defaultErrorHandlerInterceptor: DefaultErrorHandlerInterceptor): Retrofit

    fun provideDefaultErrorHandlerInterceptor(connectivityManager: ConnectivityManager, eventBus: EventBus): DefaultErrorHandlerInterceptor

    fun provideEventbus(): EventBus

    fun provideConnectivityManager(): ConnectivityManager

    fun provideService(retrofit: Retrofit): GitHubService

    fun provideSharedPreferences(): SharedPreferences

    fun provideRxSharedPreferences(sharedPreferences: SharedPreferences): RxSharedPreferencesWrapper

    fun provideDatabase(): Database

    fun provideResponseProcessor(): ResponseProcessor

}