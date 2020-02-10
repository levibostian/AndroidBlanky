package com.levibostian.androidblanky.di

import com.levibostian.androidblanky.service.service.FirebaseMessagingService
import com.levibostian.androidblanky.view.ui.MainApplication
import com.levibostian.androidblanky.view.ui.activity.LaunchActivity
import com.levibostian.androidblanky.view.ui.activity.auth.AuthenticatorActivity
import com.levibostian.androidblanky.view.ui.activity.auth.LogoutAccountActivity
import com.levibostian.androidblanky.view.ui.activity.auth.PasswordlessEmailLoginActivity
import com.levibostian.androidblanky.view.ui.activity.auth.UnlockLoggedInAccountActivity
import com.levibostian.androidblanky.view.ui.dialog.AreYouSureLogoutWendyDialogFragment
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidModule::class,
            DatabaseModule::class,
            DependencyModule::class,
            NetworkModule::class,
            ViewModelModule::class
        ]
)
interface AppGraph {
    fun inject(mainApplication: MainApplication)
    fun inject(launchActivity: LaunchActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(authenticatorActivity: AuthenticatorActivity)
    fun inject(logoutAccountActivity: LogoutAccountActivity)
    fun inject(firebaseMessagingService: FirebaseMessagingService)
    fun inject(passwordlessEmailLoginActivity: PasswordlessEmailLoginActivity)
    fun inject(unlockLoggedInAccountActivity: UnlockLoggedInAccountActivity)
    fun inject(areYouSureLogoutWendyDialogFragment: AreYouSureLogoutWendyDialogFragment)
}