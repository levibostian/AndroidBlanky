package com.levibostian.androidblanky.module

import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.levibostian.androidblanky.service.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.db.manager.RealmInstanceManager
import com.levibostian.androidblanky.service.manager.UserCredsManager
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.wrapper.LooperWrapper
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

    fun provideRealmWrapper(userManager: UserManager): RealmInstanceManager

    fun provideLooperWrapper(): LooperWrapper

}