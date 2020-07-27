package com.app.di

import android.content.Context
import androidx.room.Room
import com.app.service.db.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationContext::class)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database {
        return Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
    }
}
