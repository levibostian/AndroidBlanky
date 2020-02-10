package com.levibostian.androidblanky.di

import android.content.Context
import android.net.ConnectivityManager
import com.levibostian.androidblanky.service.AppService
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.ServiceProvider
import com.levibostian.androidblanky.service.interceptor.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.manager.UserManager
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus

@Module
class NetworkModule {

    @Provides
    fun provideAppService(serviceProvider: ServiceProvider): AppService {
        return serviceProvider.get()
    }

    @Provides
    fun provideGitHubService(serviceProvider: ServiceProvider): GitHubService {
        return serviceProvider.getGitHubService()
    }

}
