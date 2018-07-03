package com.levibostian.androidblanky.module

import com.levibostian.androidblanky.module.ManagerModule
import com.levibostian.androidblanky.view.ui.activity.LaunchActivity
import com.levibostian.androidblanky.view.ui.activity.MainActivity
import com.levibostian.androidblanky.view.ui.activity.auth.AuthenticatorActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeLaunchActivity(): LaunchActivity

    @ContributesAndroidInjector
    abstract fun contributeAuthenticatorActivity(): AuthenticatorActivity

}