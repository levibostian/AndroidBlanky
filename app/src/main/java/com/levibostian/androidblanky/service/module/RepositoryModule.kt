package com.levibostian.androidblanky.service.module

import android.content.Context
import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.datasource.ReposDataSource
import com.levibostian.androidblanky.service.repository.RepoRepository

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module class RepositoryModule(private val context: Context) {

    @Provides fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides fun provideRepoRepository(reposDataSource: ReposDataSource, gitHubUsernameDataSource: GitHubUsernameDataSource, compositeDisposable: CompositeDisposable): RepoRepository {
        return RepoRepository(reposDataSource, gitHubUsernameDataSource, compositeDisposable)
    }

}
