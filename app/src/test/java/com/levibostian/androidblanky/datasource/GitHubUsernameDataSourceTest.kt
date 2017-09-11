package com.levibostian.androidblanky.datasource

import android.annotation.SuppressLint
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.SharedPreferences
import android.util.Log
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.RealmInstanceWrapper
import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.datasource.ReposDataSource
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import io.reactivex.Observable
import io.realm.Realm
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import timber.log.Timber
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class GitHubUsernameDataSourceTest {

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var rxSharedPreferences: RxSharedPreferences
    @Mock private lateinit var sharedPreferences: SharedPreferences
    @Mock private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    @Mock private lateinit var preference: Preference<String>

    // @Captor private lateinit var userArgumentCaptor: ArgumentCaptor<RepoRepository>

    private lateinit var dataSource: GitHubUsernameDataSource

    @Before fun setUp() {
        dataSource = GitHubUsernameDataSource(rxSharedPreferences, sharedPreferences)
    }

    @Test fun getData_nothingSetDefaultString() {
        `when`(rxSharedPreferences.getString(SharedPrefersKeys.gitHubUsernameKey)).thenReturn(preference)
        `when`(preference.asObservable()).thenReturn(Observable.just(""))

        dataSource.getData()
                .test()
                .assertResult("")
    }

    @Test fun getData_getDataFromSharedPrefs() {
        `when`(rxSharedPreferences.getString(SharedPrefersKeys.gitHubUsernameKey)).thenReturn(preference)
        `when`(preference.asObservable()).thenReturn(Observable.just("username"))

        dataSource.getData()
                .test()
                .assertResult("username")
    }

    @SuppressLint("CommitPrefEdits")
    @Test fun saveData_saveToSharedPreferences() {
        `when`(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
        `when`(sharedPreferencesEditor.putString(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(sharedPreferencesEditor)

        dataSource.saveData("username")
                .test()
                .assertComplete()

        verify(sharedPreferencesEditor).putString(SharedPrefersKeys.gitHubUsernameKey, "username")
        verify(sharedPreferencesEditor).commit()
    }

    @Test fun fetchNewData_doNothing() {
        dataSource.fetchNewData(GitHubUsernameDataSource.GitHubUsernameFetchDataRequirements())
                .test()
                .assertComplete()
    }

    @Test fun cleanup_clearAndCleanup() {
        dataSource.cleanup()
    }

}