package com.levibostian.androidblanky.di

import android.content.Context
import com.levibostian.androidblanky.service.error.network.type.ConflictResponseErrorTypeAdapter
import com.levibostian.androidblanky.service.logger.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import java.util.*

@Module
class DependencyModule {

    @Provides
    fun provideEventBus(): EventBus {
        return EventBus.getDefault()
    }

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
                .add(Date::class.java, Rfc3339DateJsonAdapter())
                .add(ConflictResponseErrorTypeAdapter())
                .build()
    }

    @Provides
    fun provideLogger(context: Context): Logger {
        val loggers = listOf(
                CrashlyticsLogger(),
                FirebaseLogger(context),
                LogcatLogger()
        )

        return AppLogger(loggers)
    }

}
