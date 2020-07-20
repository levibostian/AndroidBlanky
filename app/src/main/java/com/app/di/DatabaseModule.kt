package com.app.di

import android.content.Context
import com.app.service.db.Database
import com.app.service.db.manager.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database {
        return DatabaseManager().dbInstance(context)
    }
}
