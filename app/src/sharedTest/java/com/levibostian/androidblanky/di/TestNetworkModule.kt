package com.levibostian.androidblanky.di

import com.levibostian.androidblanky.mock.MockWebServer
import com.levibostian.androidblanky.service.AppService
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.ServiceProvider
import com.levibostian.androidblanky.service.json.JsonAdapter
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
