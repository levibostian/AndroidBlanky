package com.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ExampleJVMTest: com.app.Test() {

    override fun provideTestClass(): Any = this
    @get:Rule val runRules = testRules

    @Test
    fun exampleTest() {
        assertThat("foo").isEqualTo("foo")
    }

}
