package com.levibostian.di

import android.content.Context
import android.net.ConnectivityManager
import com.levibostian.service.AppService
import com.levibostian.service.GitHubService
import com.levibostian.service.ServiceProvider
import com.levibostian.service.interceptor.DefaultErrorHandlerInterceptor
import com.levibostian.service.manager.UserManager
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides

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
