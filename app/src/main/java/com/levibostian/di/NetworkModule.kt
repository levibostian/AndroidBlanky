package com.levibostian.di

import com.levibostian.Env
import com.levibostian.service.api.AppService
import com.levibostian.service.api.GitHubService
import com.levibostian.service.ServiceProvider
import com.levibostian.service.api.ApiHostname
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideHostName(): ApiHostname = ApiHostname(Env.apiEndpoint)

}
