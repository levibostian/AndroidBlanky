package com.app.di

import android.content.Context
import com.app.Env
import com.app.extensions.isDevelopment
import com.app.service.api.ApiHostname
import com.app.service.api.GitHubService
import com.app.service.interceptor.AppendHeadersInterceptor
import com.app.service.interceptor.DefaultErrorHandlerInterceptor
import com.app.service.interceptor.HttpLoggerInterceptor
import com.app.service.json.JsonAdapter
import com.app.service.logger.*
import com.app.service.manager.UserManager
import com.app.service.util.ConnectivityUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.levibostian.boquila.RemoteConfigAdapter
import com.levibostian.boquila.plugins.LoggingRemoteConfigAdapterPlugin
import com.levibostian.boquila.plugins.RemoteConfigAdapterPlugin
import com.levibostian.firebaseboquilaadapter.FirebaseRemoteConfigAdapter
import com.levibostian.moshiboquilaplugin.MoshiRemoteConfigAdapterPlugin
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object DependencyModule {

    @Provides
    fun provideLogger(@ApplicationContext context: Context): Logger {
        val loggers = mutableListOf(
            CrashlyticsLogger(),
            FirebaseLogger(context)
        )

        if (Env.isDevelopment) {
            loggers.add(LogcatLogger())
        }

        return AppLogger(loggers)
    }

    @Provides
    fun provideRetrofit(@ApplicationContext context: Context, connectivityUtil: ConnectivityUtil, userManager: UserManager, apiHostname: ApiHostname, logger: Logger): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggerInterceptor(logger))
            .addInterceptor(DefaultErrorHandlerInterceptor(context, connectivityUtil))
            .addNetworkInterceptor(AppendHeadersInterceptor(userManager))
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(apiHostname.hostname)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(MoshiConverterFactory.create(JsonAdapter.moshi))
            .build()
    }

    @Provides
    fun provideGitHubService(retrofit: Retrofit): GitHubService = retrofit.create(GitHubService::class.java)

    @Provides
    fun provideMoshi(): Moshi = JsonAdapter.moshi

    @Provides
    fun provideRemoteConfigPlugins(moshi: Moshi): BoquilaRemoteConfigAdapterPlugins {
        val plugins: MutableList<RemoteConfigAdapterPlugin> = mutableListOf(
            MoshiRemoteConfigAdapterPlugin(moshi)
        )

        if (Env.isDevelopment) plugins.add(LoggingRemoteConfigAdapterPlugin())

        return BoquilaRemoteConfigAdapterPlugins(plugins)
    }

    @Provides
    fun provideRemoteConfig(plugins: BoquilaRemoteConfigAdapterPlugins): RemoteConfigAdapter {
        val firebaseRemoteConfig = Firebase.remoteConfig
        if (Env.isDevelopment) {
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
            firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        }

        return FirebaseRemoteConfigAdapter(firebaseRemoteConfig, plugins = plugins.plugins)
    }
}

// Exists for Dagger. Dagger does better with concrete data types like a class instead of a List<> type.
data class BoquilaRemoteConfigAdapterPlugins(val plugins: List<RemoteConfigAdapterPlugin>)
