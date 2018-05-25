package com.levibostian.androidblanky.module

import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.viewmodel.ViewModelFactory

interface ViewModelModule {

    fun provideViewModelFactory(repoRepository: ReposRepository,
                                gitHubUsernameRepository: GitHubUsernameRepository): ViewModelFactory

}