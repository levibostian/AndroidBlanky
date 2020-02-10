package com.levibostian.androidblanky.di

import android.content.Context
import dagger.Module
import dagger.Provides
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.db.manager.DatabaseManager
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database {
        return DatabaseManager().dbInstance(context)
    }

}
