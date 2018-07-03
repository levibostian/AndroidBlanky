package com.levibostian.androidblanky.service.db.module

import android.app.Application
import androidx.room.Room
import com.levibostian.androidblanky.module.DatabaseModule
import com.levibostian.androidblanky.module.ServiceModule
import com.levibostian.androidblanky.service.dao.ReposDao
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.db.manager.DatabaseManager
import com.levibostian.androidblanky.view.ui.MainApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module class AppDatabaseModule: DatabaseModule {

    @Provides override fun provideDatabase(application: Application): Database {
        return DatabaseManager().dbInstance(application)
    }

}