package com.levibostian.androidblanky.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import com.levibostian.androidblanky.service.db.Database
import javax.inject.Singleton

@Module
class TestDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database {
        return Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
    }

}
