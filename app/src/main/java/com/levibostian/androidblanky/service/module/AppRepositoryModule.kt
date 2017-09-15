package com.levibostian.androidblanky.service.module

import android.content.Context
import com.levibostian.androidblanky.module.RepositoryModule
import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.datasource.ReposDataSource
import com.levibostian.androidblanky.service.repository.RepoRepository

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module class AppRepositoryModule(private val context: Context): RepositoryModule {

    @Provides override fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides override fun provideRepoRepository(reposDataSource: ReposDataSource, gitHubUsernameDataSource: GitHubUsernameDataSource, compositeDisposable: CompositeDisposable): RepoRepository {
        return RepoRepository(reposDataSource, gitHubUsernameDataSource, compositeDisposable)
    }

}
