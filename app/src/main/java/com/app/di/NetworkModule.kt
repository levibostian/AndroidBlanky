package com.app.di

import com.app.Env
import com.app.service.api.ApiHostname
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideHostName(): ApiHostname = ApiHostname(Env.apiEndpoint)
}
