package com.levibostian.androidblanky.di

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import dagger.Module
import dagger.Provides
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.db.manager.DatabaseManager
import javax.inject.Singleton

@Module
class TestDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): Database {
        return Room.inMemoryDatabaseBuilder(context, Database::class.java)
                .allowMainThreadQueries()
                .build()
    }

}
