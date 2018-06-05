package com.levibostian.androidblanky.service.manager

import android.annotation.SuppressLint
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.SharedPreferences
import com.google.common.truth.Truth
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserManagerTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var sharedPrefs: SharedPreferences
    @Mock private lateinit var sharedPrefsEditor: SharedPreferences.Editor

    @Captor private lateinit var userArgumentCaptor: ArgumentCaptor<RepoModel>

    private lateinit var manager: UserManager

    @Before
    fun setUp() {
        manager = UserManager(sharedPrefs)
    }

    @Test
    fun authToken_getNullNotSet() {
        Mockito.`when`(sharedPrefs.getString(SharedPrefersKeys.USER_ID, null)).thenReturn(null)

        Truth.assertThat(manager.id)
                .isNull()
    }

    @SuppressLint("CommitPrefEdits")
    @Test
    fun authToken_commitTokenAfterSetting() {
        val id = "12345"

        Mockito.`when`(sharedPrefs.edit()).thenReturn(sharedPrefsEditor)
        Mockito.`when`(sharedPrefsEditor.putString(SharedPrefersKeys.USER_ID, id)).thenReturn(sharedPrefsEditor)

        manager.id = id

        Mockito.verify(sharedPrefsEditor).putString(SharedPrefersKeys.USER_ID, id)
        Mockito.verify(sharedPrefsEditor).commit()
    }

}