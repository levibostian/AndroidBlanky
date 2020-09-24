package com.app.di

import android.app.Application
import android.content.Context
import com.app.Env
import com.app.service.ResetAppRunner
import com.app.service.api.GitHubApiHostname
import com.app.service.pendingtasks.PendingTasks
import com.app.service.pendingtasks.PendingTasksFactory
import com.app.service.pendingtasks.WendyPendingTasks
import com.app.view.ui.MainApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideGitHubHostname(): GitHubApiHostname = GitHubApiHostname(Env.apiEndpoint)

    @Provides
    fun providePendingTasks(@ApplicationContext context: Context, pendingTasksFactory: PendingTasksFactory): PendingTasks = WendyPendingTasks(context, pendingTasksFactory)

    @Provides
    fun provideAppResetRunner(application: Application): ResetAppRunner = (application as MainApplication)
}
