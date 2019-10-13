package com.levibostian.androidblanky.module

import android.accounts.AccountManager
import com.levibostian.androidblanky.service.manager.AppDeviceAccountManager
import com.levibostian.androidblanky.service.manager.DeviceAccountManager
import com.levibostian.androidblanky.service.manager.NotificationChannelManager
import com.levibostian.androidblanky.service.manager.UserManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object ManagerModule {

    fun get(): Module {
        return module {
            factory { AppDeviceAccountManager(get()) as DeviceAccountManager }
            factory { UserManager(get(), get()) }
            factory { NotificationChannelManager(androidContext()) }
        }
    }

}