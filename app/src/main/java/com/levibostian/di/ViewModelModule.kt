package com.levibostian.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.levibostian.viewmodel.GitHubUsernameViewModel
import com.levibostian.viewmodel.ReposViewModel
import com.levibostian.viewmodel.UserViewModel
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