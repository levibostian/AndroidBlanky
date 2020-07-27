package com.app.di

import com.app.mock.MockWebServer
import com.app.service.api.ApiHostname
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationContext::class)
object TestNetworkModule {

    @Provides
    @Singleton
    fun provideMockWebserver(): MockWebServer {
        return MockWebServer(okhttp3.mockwebserver.MockWebServer())
    }

    @Provides
    fun provideHostName(mockWebServer: MockWebServer): ApiHostname {
        return ApiHostname(mockWebServer.url)
    }
}
