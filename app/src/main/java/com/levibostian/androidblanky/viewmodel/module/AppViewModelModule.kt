package com.levibostian.androidblanky.viewmodel.module

import android.content.Context
import com.levibostian.androidblanky.module.ViewModelModule
import com.levibostian.androidblanky.service.repository.RepoRepository
import com.levibostian.androidblanky.viewmodel.ViewModelFactory
import com.levibostian.androidblanky.viewmodel.ViewModelProviderWrapper

import dagger.Module
import dagger.Provides

@Module class AppViewModelModule(private val context: Context): ViewModelModule {

    @Provides override fun provideViewModelFactory(repoRepository: RepoRepository): ViewModelFactory {
        return ViewModelFactory(repoRepository)
    }

    @Provides override fun provideViewModelProviderWrapper(): ViewModelProviderWrapper {
        return ViewModelProviderWrapper()
    }

}


