package com.levibostian.androidblanky.module

import org.greenrobot.eventbus.EventBus
import org.koin.core.module.Module
import org.koin.dsl.module

object LibraryModule {

    fun get(): Module {
        return module {
            factory { EventBus.getDefault() }
        }
    }

}