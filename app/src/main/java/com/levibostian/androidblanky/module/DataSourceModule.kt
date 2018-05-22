package com.levibostian.androidblanky.module

import android.content.SharedPreferences
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.datasource.ReposDataSource
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper

interface DataSourceModule {

    fun provideReposDataSource(sharedPreferences: SharedPreferences, gitHubService: GitHubService): ReposDataSource

    fun provideGitHubUsernameDataSource(rxSharedPreferencesWrapper: RxSharedPreferencesWrapper, sharedPreferences: SharedPreferences): GitHubUsernameDataSource

}