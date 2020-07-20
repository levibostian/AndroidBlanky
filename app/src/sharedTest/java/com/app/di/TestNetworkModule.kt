package com.app.di

import com.app.mock.MockWebServer
import com.app.service.ServiceProvider
import com.app.service.api.AppService
import com.app.service.api.GitHubService
import com.app.service.json.JsonAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestNetworkModule {

    @Provides
    @Singleton
    fun provideMockWebserver(jsonAdapter: JsonAdapter): MockWebServer {
        return MockWebServer(okhttp3.mockwebserver.MockWebServer(), jsonAdapter)
    }

    @Provides
    @Singleton
    fun provideAppService(serviceProvider: ServiceProvider, mockWebServer: MockWebServer): AppService {
        return serviceProvider.get(mockWebServer.url)
    }

    @Provides
    @Singleton
    fun provideGitHubService(serviceProvider: ServiceProvider): GitHubService {
        return serviceProvider.getGitHubService()
    }
}
