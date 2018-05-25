package com.levibostian.androidblanky.service.module

import android.content.Context
import com.levibostian.androidblanky.module.RepositoryModule
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.ResponseProcessor
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module class AppRepositoryModule(private val context: Context): RepositoryModule {

    @Provides override fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides override fun provideRepoRepository(responseProcessor: ResponseProcessor, service: GitHubService, db: Database): ReposRepository {
        return ReposRepository(responseProcessor, service, db)
    }

    @Provides override fun provideGithubUsernameRepository(): GitHubUsernameRepository {
        return GitHubUsernameRepository(context)
    }

}
