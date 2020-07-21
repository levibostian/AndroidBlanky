package com.app

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.app.rule.RxImmediateSchedulerRule
import com.app.rule.TellerUnitTestRule
import com.app.service.KeyValueStorage
import com.app.service.SchedulerProvider
import com.app.view.ui.MainApplication
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

abstract class UnitTest {

    @get:Rule val rule = InstantTaskExecutorRule()
    @get:Rule val mockitoRule = MockitoJUnit.rule()
    @get:Rule val tellerTest = TellerUnitTestRule()
    @get:Rule val rxRule = RxImmediateSchedulerRule()
    @Mock lateinit var schedulerProvider: SchedulerProvider

    // We want to perform unit tests with a real instance of this class because (1) mocking each save and get function is super annoying and verifying each call is tedious and error prone. By using a real instance that Robolectric can give you, we can run more natural tests that behave close to the real thing.
    protected val keyValueStorage: KeyValueStorage
        get() = KeyValueStorage(PreferenceManager.getDefaultSharedPreferences(context))

    protected val context: Context
        get() = getApplicationContext<MainApplication>()

    @Before
    open fun setup() {
        keyValueStorage.deleteAll()

        whenever(schedulerProvider.background).thenReturn(Schedulers.trampoline())
        whenever(schedulerProvider.main).thenReturn(Schedulers.trampoline())
    }
}
