package com.levibostian.androidblanky.module

import org.koin.core.module.Module


object AppModules {

    fun get(): List<Module> {
        return listOf(
                DatabaseModule.get(),
                ManagerModule.get(),
                RepositoryModule.get(),
                ServiceModule.get(),
                ViewModelModule.get()
        )
    }

}