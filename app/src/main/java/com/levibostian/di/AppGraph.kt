package com.levibostian.di

import com.levibostian.service.service.FirebaseMessagingService
import com.levibostian.view.ui.MainApplication
import com.levibostian.view.ui.activity.BaseActivity
import com.levibostian.view.ui.activity.LaunchActivity
import com.levibostian.view.ui.dialog.AreYouSureLogoutWendyDialogFragment
import com.levibostian.view.ui.fragment.MainFragment
import com.levibostian.view.ui.fragment.SettingsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidModule::class,
            DatabaseModule::class,
            DependencyModule::class,
            NetworkModule::class,
            ViewModelModule::class,
            WorkerModule::class
        ]
)
interface AppGraph {
    fun inject(launchActivity: LaunchActivity)
    fun inject(mainApplication: MainApplication)
    fun inject(mainFragment: MainFragment)
    fun inject(baseActivity: BaseActivity)
    fun inject(firebaseMessagingService: FirebaseMessagingService)
    fun inject(areYouSureLogoutWendyDialogFragment: AreYouSureLogoutWendyDialogFragment)
    fun inject(settingsFragment: SettingsFragment)
}