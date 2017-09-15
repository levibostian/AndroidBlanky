package com.levibostian.androidblanky.module

import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.datasource.ReposDataSource
import com.levibostian.androidblanky.service.repository.RepoRepository
import io.reactivex.disposables.CompositeDisposable

interface RepositoryModule {

    fun provideCompositeDisposable(): CompositeDisposable

    fun provideRepoRepository(reposDataSource: ReposDataSource, gitHubUsernameDataSource: GitHubUsernameDataSource, compositeDisposable: CompositeDisposable): RepoRepository

}