package com.levibostian.androidblanky.viewmodel.module


import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.levibostian.androidblanky.module.ViewModelModule
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.viewmodel.TestViewModelFactory

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class MockViewModelModule(private val context: Context): ViewModelModule {

    @Provides @Singleton override fun provideViewModelFactory(repoRepository: ReposRepository,
                                                              gitHubUsernameRepository: GitHubUsernameRepository): ViewModelProvider.Factory {
        return TestViewModelFactory(repoRepository, gitHubUsernameRepository)
    }

}
