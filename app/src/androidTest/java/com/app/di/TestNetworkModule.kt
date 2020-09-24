package com.app.di

import com.app.mock.MockWebServer
import com.app.service.ResetAppRunner
import com.app.service.api.GitHubApiHostname
import com.app.service.pendingtasks.PendingTasks
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.mockito.Mockito
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TestNetworkModule {

    @Provides
    @Singleton
    fun provideMockWebserver(): MockWebServer = MockWebServer(okhttp3.mockwebserver.MockWebServer())

    @Provides
    @Singleton
    fun provideGitHubHostname(mockWebServer: MockWebServer): GitHubApiHostname = GitHubApiHostname(mockWebServer.url)

    @Provides
    @Singleton
    fun providePendingTasks(): PendingTasks = Mockito.mock(PendingTasks::class.java)

    @Provides
    @Singleton
    fun provideAppResetRunner(): ResetAppRunner = Mockito.mock(ResetAppRunner::class.java)
}
