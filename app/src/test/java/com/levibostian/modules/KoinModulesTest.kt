package com.levibostian.modules

import android.accounts.AccountManager
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.levibostian.module.AppModules
import com.levibostian.teller.Teller
import org.greenrobot.eventbus.EventBus
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

    @Before
    fun setup() {
        Teller.initUnitTesting()
    }

    @Test
    fun `check modules to make sure graph complete`() {
        val mockAndroidModule = module {
            // Mock Android specific stuff. https://github.com/InsertKoinIO/koin/issues/241#issuecomment-425363650
            single { mock(Context::class.java) }
            single { mock(Application::class.java) }

            // Mock dependencies in AndroidModule
            factory(override = true) { mock(AccountManager::class.java) }
            factory(override = true) { mock(SharedPreferences::class.java) }
            factory(override = true) { mock(ConnectivityManager::class.java) }

            // Mock dependencies in LibraryModule
            factory(override = true) { mock(EventBus::class.java) }
        }

        startKoin {
            modules(AppModules.get() + mockAndroidModule)
        }.checkModules()
    }

}