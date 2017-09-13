package com.levibostian.androidblanky.service.module

import android.content.Context
import android.content.SharedPreferences
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.db.manager.RealmInstanceManager
import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.datasource.ReposDataSource
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper

import dagger.Module
import dagger.Provides

@Module class DataSourceModule(private val context: Context) {

    @Provides fun provideReposDataSource(sharedPreferences: SharedPreferences, gitHubService: GitHubService, realmInstanceManager: RealmInstanceManager): ReposDataSource {
        return ReposDataSource(sharedPreferences, gitHubService, realmInstanceManager)
    }

    @Provides fun provideGitHubUsernameDataSource(rxSharedPreferencesWrapper: RxSharedPreferencesWrapper, sharedPreferences: SharedPreferences): GitHubUsernameDataSource {
        return GitHubUsernameDataSource(rxSharedPreferencesWrapper, sharedPreferences)
    }

}
