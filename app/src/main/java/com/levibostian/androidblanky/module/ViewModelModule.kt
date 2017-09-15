package com.levibostian.androidblanky.module

import com.levibostian.androidblanky.service.repository.RepoRepository
import com.levibostian.androidblanky.viewmodel.ViewModelFactory
import com.levibostian.androidblanky.viewmodel.ViewModelProviderWrapper

interface ViewModelModule {

    fun provideViewModelFactory(repoRepository: RepoRepository): ViewModelFactory

    fun provideViewModelProviderWrapper(): ViewModelProviderWrapper

}