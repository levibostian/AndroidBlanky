package com.levibostian.androidblanky.viewmodel.module

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.levibostian.androidblanky.module.ViewModelModule
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.viewmodel.ViewModelFactory

import dagger.Module
import dagger.Provides

@Module class AppViewModelModule(private val context: Context): ViewModelModule {

    @Provides override fun provideViewModelFactory(repoRepository: ReposRepository,
                                                   gitHubUsernameRepository: GitHubUsernameRepository): ViewModelProvider.Factory {
        return ViewModelFactory(repoRepository, gitHubUsernameRepository)
    }

}
