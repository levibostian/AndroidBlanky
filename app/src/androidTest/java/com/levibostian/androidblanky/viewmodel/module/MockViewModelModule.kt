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
import com.levibostian.androidblanky.viewmodel.TestViewModelFactory
import com.levibostian.androidblanky.viewmodel.ViewModelFactory
import dagger.Binds

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import org.mockito.Mockito
import javax.inject.Singleton

@Module
class MockViewModelModule {

    @Provides
    @Singleton
    fun provideTestViewModelFactory(): ViewModelProvider.Factory {
        return TestViewModelFactory()
    }

}