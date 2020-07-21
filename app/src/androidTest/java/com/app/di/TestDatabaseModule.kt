package com.app.di

import android.content.Context
import androidx.room.Room
import com.levibostian.service.db.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database {
        return Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
    }
}
