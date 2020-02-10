package com.levibostian.androidblanky.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.androidblanky.viewmodel.UserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ReposViewModel::class)
    abstract fun bindReposViewModel(viewmodel: ReposViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(viewmodel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GitHubUsernameViewModel::class)
    abstract fun bindGitHubUsernameViewModel(viewmodel: GitHubUsernameViewModel): ViewModel

}