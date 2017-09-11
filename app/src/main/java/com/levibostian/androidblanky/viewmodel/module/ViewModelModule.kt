package com.levibostian.androidblanky.viewmodel.module

import android.content.Context
import com.levibostian.androidblanky.service.RealmInstanceWrapper
import com.levibostian.androidblanky.service.repository.RepoRepository
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import com.levibostian.androidblanky.viewmodel.ViewModelFactory

import dagger.Module
import dagger.Provides
import io.realm.Realm

@Module class ViewModelModule(private val context: Context) {

    @Provides fun provideViewModelFactory(repoRepository: RepoRepository): ViewModelFactory {
        return ViewModelFactory(repoRepository)
    }

}


