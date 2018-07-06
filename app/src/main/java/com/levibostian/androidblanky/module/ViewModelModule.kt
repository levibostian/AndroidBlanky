package com.levibostian.androidblanky.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.androidblanky.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import dagger.Provides
import dagger.multibindings.StringKey

interface ViewModelModule {

    fun bindReposViewModel(reposViewModel: ReposViewModel): ViewModel

    fun bindGitHubUsernameViewModel(gitHubUsernameViewModel: GitHubUsernameViewModel): ViewModel

    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}