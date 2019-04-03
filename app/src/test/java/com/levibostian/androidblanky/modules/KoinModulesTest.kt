package com.levibostian.androidblanky.modules

import android.app.Application
import android.content.Context
import com.levibostian.androidblanky.module.AppModules
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.check.checkModules
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class KoinModulesTest {

    @Test
    fun `check modules to make sure graph complete`() {
        // Mock Android specific stuff. https://github.com/InsertKoinIO/koin/issues/241#issuecomment-425363650
        val mockAndroidModule = module {
            single { mock(Context::class.java) }
            single { mock(Application::class.java) }
        }

        startKoin {
            modules(AppModules.get() + mockAndroidModule)
        }.checkModules()
    }

}