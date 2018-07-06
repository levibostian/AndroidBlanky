package com.levibostian.androidblanky.viewmodel.module

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.levibostian.androidblanky.module.ViewModelKey
import com.levibostian.androidblanky.module.ViewModelModule
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.androidblanky.viewmodel.ViewModelFactory
import dagger.Binds

import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class AppViewModelModule: ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ReposViewModel::class)
    abstract override fun bindReposViewModel(reposViewModel: ReposViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GitHubUsernameViewModel::class)
    abstract override fun bindGitHubUsernameViewModel(gitHubUsernameViewModel: GitHubUsernameViewModel): ViewModel

    @Binds
    abstract override fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}
