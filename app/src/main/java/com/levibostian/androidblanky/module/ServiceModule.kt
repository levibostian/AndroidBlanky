package com.levibostian.androidblanky.module

import android.content.Context
import android.net.ConnectivityManager
import androidx.preference.PreferenceManager
import com.levibostian.androidblanky.service.DataDestroyer
import com.levibostian.androidblanky.service.interceptor.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.ServiceProvider
import com.levibostian.androidblanky.service.error.network.type.ConflictResponseErrorTypeAdapter
import com.levibostian.androidblanky.service.json.JsonAdapter
import com.levibostian.androidblanky.service.util.ResponseProcessor
import com.levibostian.androidblanky.service.logger.*
import com.levibostian.androidblanky.service.pendingtasks.PendingTasksFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import java.util.*

object ServiceModule {

    fun get(): Module {
        return module {
            factory { PendingTasksFactory(get(), get()) }
            factory { EventBus.getDefault() }
            factory { androidApplication().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
            factory { DefaultErrorHandlerInterceptor(androidContext(), get(), get()) }
            factory { ServiceProvider(get(), get(), get()).get() }
            factory { ServiceProvider(get(), get(), get()).getGitHubService() }
            factory { PreferenceManager.getDefaultSharedPreferences(androidApplication()) }
            factory {
                Moshi.Builder()
                    .add(Date::class.java, Rfc3339DateJsonAdapter())
                    .add(ConflictResponseErrorTypeAdapter())
                    .build()
            }
            factory { JsonAdapter(get()) }
            factory { ResponseProcessor(androidContext(), get(), get()) }
            factory { DataDestroyer(get(), get(), get(), get()) }
            factory {
                AppLogger(listOf(
                        FirebaseLogger(androidContext()),
                        CrashlyticsLogger(),
                        LogcatLogger()
                ))
            }
        }
    }

}