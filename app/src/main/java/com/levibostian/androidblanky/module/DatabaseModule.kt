package com.levibostian.androidblanky.module

import android.app.Application
import com.levibostian.androidblanky.service.dao.ReposDao
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.db.manager.DatabaseManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

object DatabaseModule {

    fun get(): Module {
        return module {
            factory { DatabaseManager().dbInstance(androidContext()) }
        }
    }

}