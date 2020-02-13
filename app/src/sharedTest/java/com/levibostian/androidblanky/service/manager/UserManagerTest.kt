package com.levibostian.androidblanky.service.manager

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import com.levibostian.androidblanky.service.model.SharedPrefersKeys
import com.levibostian.androidblanky.util.SharedPreferencesMock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserManagerTest {

    @Mock private lateinit var deviceAccountManager: DeviceAccountManager

    private val sharedPrefsMock = SharedPreferencesMock()
    private val sharedPrefs = sharedPrefsMock.sharedPrefs
    private val sharedPrefsEditor = sharedPrefsMock.editor

    private lateinit var manager: UserManager

    @Before
    fun setUp() {
        manager = UserManager(sharedPrefs, deviceAccountManager)
    }

    @Test
    fun authToken_getNullNotSet() {
        whenever(sharedPrefs.getString(SharedPrefersKeys.USER_ID, null)).thenReturn(null)

        assertThat(manager.id).isNull()
    }

    @SuppressLint("CommitPrefEdits")
    @Test
    fun authToken_commitTokenAfterSetting() {
        val id = "12345"

        manager.id = id

        verify(sharedPrefsEditor).putString(SharedPrefersKeys.USER_ID, id)
        verify(sharedPrefsEditor).commit()
    }

}