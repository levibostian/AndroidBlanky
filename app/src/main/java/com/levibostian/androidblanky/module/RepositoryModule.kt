package com.levibostian.androidblanky.module

import android.app.Application
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.util.ResponseProcessor
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.service.repository.UserRepository
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object RepositoryModule {

    fun get(): Module {
        return module {
            factory { ReposRepository(get(), get(), get()) }
            factory { GitHubUsernameRepository(get()) }
            factory { UserRepository(get(), get()) }
        }
    }

}