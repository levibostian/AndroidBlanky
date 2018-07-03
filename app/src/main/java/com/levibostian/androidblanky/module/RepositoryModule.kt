package com.levibostian.androidblanky.module

import android.app.Application
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.util.ResponseProcessor
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository
import io.reactivex.disposables.CompositeDisposable

interface RepositoryModule {

    fun provideCompositeDisposable(): CompositeDisposable

    fun provideRepoRepository(responseProcessor: ResponseProcessor, service: GitHubService, db: Database): ReposRepository

    fun provideGithubUsernameRepository(application: Application): GitHubUsernameRepository

}