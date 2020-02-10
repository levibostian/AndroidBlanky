package com.levibostian.androidblanky.service

import android.content.Context
import android.net.ConnectivityManager
import com.levibostian.androidblanky.BuildConfig
import com.levibostian.androidblanky.service.interceptor.AppendHeadersInterceptor
import com.levibostian.androidblanky.service.interceptor.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.AppConstants
import com.levibostian.androidblanky.service.error.network.type.ConflictResponseErrorTypeAdapter
import com.levibostian.androidblanky.service.util.ConnectivityUtil
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Inject

class ServiceProvider @Inject constructor(private val context: Context,
                                          private val eventBus: EventBus,
                                          private val connectivityUtil: ConnectivityUtil,
                                          private val userManager: UserManager,
                                          private val moshi: Moshi) {

    fun getGitHubService(): GitHubService {
        return getService("https://api.github.com").create(GitHubService::class.java)
    }

    fun get(endpoint: String = AppConstants.API_ENDPOINT): AppService {
        return getService(endpoint).create(AppService::class.java)
    }

    private fun getService(hostname: String): Retrofit {
        val errorHandlerInterceptor = DefaultErrorHandlerInterceptor(context, eventBus, connectivityUtil)

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(errorHandlerInterceptor)
                .addNetworkInterceptor(AppendHeadersInterceptor(userManager))
                .build()

        return Retrofit.Builder()
                .client(client)
                .baseUrl(hostname)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
    }

}