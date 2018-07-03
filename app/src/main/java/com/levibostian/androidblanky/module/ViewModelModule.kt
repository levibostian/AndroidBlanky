package com.levibostian.androidblanky.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.androidblanky.viewmodel.ViewModelFactory

interface ViewModelModule {

    fun provideViewModelFactory(repoRepository: ReposRepository,
                                gitHubUsernameRepository: GitHubUsernameRepository): ViewModelProvider.Factory

}