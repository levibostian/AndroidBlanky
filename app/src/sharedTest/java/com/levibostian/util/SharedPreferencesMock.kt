package com.levibostian.util

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.whenever
import org.mockito.Mockito.mock

/**
 * Convenient class to setup the boilerplate code required for setting up shared prefs mocking.
 */
class SharedPreferencesMock {

    val sharedPrefs = mock(SharedPreferences::class.java)
    val editor = mock(SharedPreferences.Editor::class.java)

    init {
        whenever(sharedPrefs.edit()).thenReturn(editor)

        whenever(editor.putString(any(), anyOrNull())).thenReturn(editor)
        whenever(editor.putLong(any(), any())).thenReturn(editor)
        whenever(editor.putBoolean(any(), any())).thenReturn(editor)
        whenever(editor.putFloat(any(), any())).thenReturn(editor)
        whenever(editor.putStringSet(any(), any())).thenReturn(editor)
        whenever(editor.putInt(any(), any())).thenReturn(editor)

        whenever(editor.clear()).thenReturn(editor)
    }

}