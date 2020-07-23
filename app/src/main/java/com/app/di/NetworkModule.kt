package com.app.di

import com.app.Env
import com.app.service.api.ApiHostname
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideHostName(): ApiHostname = ApiHostname(Env.apiEndpoint)
}
