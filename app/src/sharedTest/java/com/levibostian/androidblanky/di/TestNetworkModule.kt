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
        // To fix the crash when running tests with robolectric with java11, `java.lang.NullPointerException: No password supplied for PKCS#12 KeyStore`. I believe this is a java11 new thing.
        // From: https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStore", "NONE")

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
