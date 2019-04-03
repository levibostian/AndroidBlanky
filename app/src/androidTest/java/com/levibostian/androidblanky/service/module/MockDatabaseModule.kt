package com.levibostian.androidblanky.service.module

import android.app.Application
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.levibostian.androidblanky.module.DatabaseModule
import com.levibostian.androidblanky.service.db.Database
import javax.inject.Singleton
import com.levibostian.androidblanky.service.interceptor.MissingDataResponseInterceptor
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper

@Module class MockDatabaseModule: DatabaseModule {

    @Provides @Singleton override fun provideDatabase(application: Application): Database {
        val context = InstrumentationRegistry.getTargetContext()
        return Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
    }

}