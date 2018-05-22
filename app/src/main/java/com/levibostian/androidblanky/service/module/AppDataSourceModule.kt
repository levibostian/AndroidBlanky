package com.levibostian.androidblanky.service.module

import android.content.Context
import android.content.SharedPreferences
import com.levibostian.androidblanky.module.DataSourceModule
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.datasource.ReposDataSource
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper

import dagger.Module
import dagger.Provides

@Module class AppDataSourceModule(private val context: Context): DataSourceModule {

    @Provides override fun provideReposDataSource(sharedPreferences: SharedPreferences, gitHubService: GitHubService, realmInstanceManager: RealmInstanceManager): ReposDataSource {
        return ReposDataSource(sharedPreferences, gitHubService, realmInstanceManager)
    }

    @Provides override fun provideGitHubUsernameDataSource(rxSharedPreferencesWrapper: RxSharedPreferencesWrapper, sharedPreferences: SharedPreferences): GitHubUsernameDataSource {
        return GitHubUsernameDataSource(rxSharedPreferencesWrapper, sharedPreferences)
    }

}
