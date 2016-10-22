package com.levibostian.androidblanky

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.levibostian.androidblanky.fragment.MainFragment
import com.levibostian.androidblanky.module.ApiModule
import com.levibostian.androidblanky.module.DaoModule
import com.levibostian.androidblanky.module.ManagerModule
import com.levibostian.androidblanky.module.UtilModule
import dagger.Component
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration

import javax.inject.Singleton

class MainApplication : Application() {

    companion object {
        @JvmStatic lateinit var component: ApplicationComponent
    }

    @Singleton
    @Component(modules = arrayOf(ApiModule::class, DaoModule::class, ManagerModule::class, UtilModule::class))
    interface ApplicationComponent {
        fun inject(mainFragment: MainFragment)
    }

    override fun onCreate() {
        super.onCreate()

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        val fabric = Fabric.Builder(this).kits(Crashlytics.Builder().core(core).build()).debuggable(true).build()
        Fabric.with(fabric)

        component = DaggerApplicationComponent.builder().apiModule(ApiModule(this)).daoModule(DaoModule()).managerModule(ManagerModule(this)).utilModule(UtilModule(this)).build()

        configureRealm()
    }

    private fun configureRealm() {
        val config = RealmConfiguration.Builder(this).schemaVersion(0).build()
        Realm.setDefaultConfiguration(config)
    }

}
