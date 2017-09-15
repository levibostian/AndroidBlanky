package com.levibostian.androidblanky.datasource

import android.annotation.SuppressLint
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.Preference
import com.google.common.truth.Truth
import com.levibostian.androidblanky.service.datasource.GitHubUsernameDataSource
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import io.reactivex.Observable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GitHubUsernameDataSourceTest {

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    //@Mock private lateinit var rxSharedPreferences: RxSharedPreferences
    @Mock private lateinit var rxSharedPreferencesWrapper: RxSharedPreferencesWrapper
    @Mock private lateinit var sharedPreferences: SharedPreferences
    @Mock private lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    @Mock private lateinit var preference: Preference<String>

    // @Captor private lateinit var userArgumentCaptor: ArgumentCaptor<RepoRepository>

    private lateinit var dataSource: GitHubUsernameDataSource

    @Before fun setUp() {
        dataSource = GitHubUsernameDataSource(rxSharedPreferencesWrapper, sharedPreferences)
    }

    @Test(expected = RuntimeException::class)
    @Throws(Exception::class)
    fun lastTimeFreshDataFetchedKey_throw() {
        dataSource.lastTimeFreshDataFetchedKey()
    }

    @Test fun deleteData_setsToDefaultString() {
        `when`(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
        `when`(sharedPreferencesEditor.putString(SharedPrefersKeys.gitHubUsernameKey, "")).thenReturn(sharedPreferencesEditor)

        dataSource.deleteData()
                .test()
                .assertComplete()

        verify(sharedPreferencesEditor).commit()
    }

    @Test(expected = RuntimeException::class)
    @Throws(Exception::class)
    fun fetchFreshDataOrFail_throw() {
        dataSource.fetchFreshData(GitHubUsernameDataSource.GitHubUsernameFetchDataRequirements())
    }

    @Test fun getData_nothingSetGetDefaultString() {
        `when`(rxSharedPreferencesWrapper.getString(SharedPrefersKeys.gitHubUsernameKey)).thenReturn(preference)
        `when`(preference.asObservable()).thenReturn(Observable.just(""))

        dataSource.getData()
                .test()
                .assertResult("")
    }

    @Test fun getData_getDataFromSharedPrefs() {
        `when`(rxSharedPreferencesWrapper.getString(SharedPrefersKeys.gitHubUsernameKey)).thenReturn(preference)
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

    @Test(expected = RuntimeException::class)
    @Throws(Exception::class)
    fun fetchNewData_cannotDo() {
        dataSource.fetchFreshData(GitHubUsernameDataSource.GitHubUsernameFetchDataRequirements())
                .test()
                .assertComplete()
    }

    @Test fun cleanup_clearAndCleanup() {
        dataSource.cleanup()
    }

}