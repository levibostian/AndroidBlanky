package com.levibostian.androidblanky.viewmodel.module

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import com.levibostian.androidblanky.service.RealmInstanceWrapper
import com.levibostian.androidblanky.service.repository.RepoRepository
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.androidblanky.viewmodel.ViewModelFactory
import com.levibostian.androidblanky.viewmodel.ViewModelProviderWrapper

import dagger.Module
import dagger.Provides
import io.realm.Realm

@Module class ViewModelModule(private val context: Context) {

    @Provides fun provideViewModelFactory(repoRepository: RepoRepository): ViewModelFactory {
        return ViewModelFactory(repoRepository)
    }

    @Provides fun provideViewModelProviderWrapper(): ViewModelProviderWrapper {
        return ViewModelProviderWrapper()
    }

}


