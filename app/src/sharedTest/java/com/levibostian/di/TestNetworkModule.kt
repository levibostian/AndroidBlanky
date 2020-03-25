package com.levibostian.di

import com.levibostian.mock.MockWebServer
import com.levibostian.service.AppService
import com.levibostian.service.GitHubService
import com.levibostian.service.ServiceProvider
import com.levibostian.service.json.JsonAdapter
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
