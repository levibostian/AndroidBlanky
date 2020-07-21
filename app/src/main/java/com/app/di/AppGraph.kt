package com.app.di

import com.app.service.FirebaseMessagingService
import com.app.view.ui.MainApplication
import com.app.view.ui.activity.LaunchActivity
import com.app.view.ui.activity.LoginActivity
import com.app.view.ui.dialog.AreYouSureLogoutWendyDialogFragment
import com.app.view.ui.fragment.MainFragment
import com.app.view.ui.fragment.SettingsFragment
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
    fun inject(firebaseMessagingService: FirebaseMessagingService)
    fun inject(areYouSureLogoutWendyDialogFragment: AreYouSureLogoutWendyDialogFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(loginActivity: LoginActivity)
}
