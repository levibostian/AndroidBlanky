package com.levibostian.androidblanky.service.module

import android.accounts.AccountManager
import android.content.SharedPreferences
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.view.ui.MainApplication
import org.mockito.Mockito
import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import com.google.firebase.analytics.FirebaseAnalytics
import com.levibostian.androidblanky.module.DatabaseModule
import com.levibostian.androidblanky.module.ServiceModule
import com.levibostian.androidblanky.service.DataDestroyer
import com.levibostian.androidblanky.service.dao.ReposDao
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.db.manager.DatabaseManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import com.levibostian.androidblanky.service.interceptor.DefaultErrorHandlerInterceptor
import com.levibostian.androidblanky.service.interceptor.MissingDataResponseInterceptor
import com.levibostian.androidblanky.service.manager.UserManager
import com.levibostian.androidblanky.service.util.ResponseProcessor
import com.levibostian.androidblanky.service.model.AppConstants
import com.levibostian.androidblanky.service.wrapper.RxSharedPreferencesWrapper
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit

@Module class MockDatabaseModule(val application: MainApplication): DatabaseModule {

    @Provides @Singleton override fun provideDatabase(): Database {
        val context = InstrumentationRegistry.getTargetContext()
        return Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
    }

}