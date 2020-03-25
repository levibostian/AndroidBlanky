package com.levibostian.di

import com.levibostian.service.service.FirebaseMessagingService
import com.levibostian.view.ui.MainApplication
import com.levibostian.view.ui.activity.LaunchActivity
import com.levibostian.view.ui.activity.auth.AuthenticatorActivity
import com.levibostian.view.ui.activity.auth.LogoutAccountActivity
import com.levibostian.view.ui.activity.auth.PasswordlessEmailLoginActivity
import com.levibostian.view.ui.activity.auth.UnlockLoggedInAccountActivity
import com.levibostian.view.ui.dialog.AreYouSureLogoutWendyDialogFragment
import com.levibostian.view.ui.fragment.MainFragment
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
    fun inject(launchActivity: LaunchActivity)
    fun inject(mainApplication: MainApplication)
    fun inject(mainFragment: MainFragment)
    fun inject(authenticatorActivity: AuthenticatorActivity)
    fun inject(logoutAccountActivity: LogoutAccountActivity)
    fun inject(firebaseMessagingService: FirebaseMessagingService)
    fun inject(passwordlessEmailLoginActivity: PasswordlessEmailLoginActivity)
    fun inject(unlockLoggedInAccountActivity: UnlockLoggedInAccountActivity)
    fun inject(areYouSureLogoutWendyDialogFragment: AreYouSureLogoutWendyDialogFragment)
}