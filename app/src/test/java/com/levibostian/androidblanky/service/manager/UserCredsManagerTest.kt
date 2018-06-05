package com.levibostian.androidblanky.service.manager

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
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserCredsManagerTest {

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var sharedPrefs: SharedPreferences
    @Mock private lateinit var sharedPrefsEditor: SharedPreferences.Editor

    @Captor private lateinit var userArgumentCaptor: ArgumentCaptor<RepoModel>

    private lateinit var credsManager: UserCredsManager

    @Before fun setUp() {
        credsManager = UserCredsManager(sharedPrefs)
    }

    @Test fun authToken_getNullNotSet() {
        `when`(sharedPrefs.getString(SharedPrefersKeys.USER_AUTH_TOKEN, null)).thenReturn(null)

        Truth.assertThat(credsManager.authToken)
                .isNull()
    }

    @Test fun authToken_commitTokenAfterSetting() {
        val token = "12345"

        `when`(sharedPrefs.edit()).thenReturn(sharedPrefsEditor)
        `when`(sharedPrefsEditor.putString(SharedPrefersKeys.USER_AUTH_TOKEN, token)).thenReturn(sharedPrefsEditor)

        credsManager.authToken = token

        verify(sharedPrefsEditor).putString(SharedPrefersKeys.USER_AUTH_TOKEN, token)
        verify(sharedPrefsEditor).commit()
    }

}
