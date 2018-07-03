package com.levibostian.androidblanky.module

import com.levibostian.androidblanky.module.ManagerModule
import com.levibostian.androidblanky.service.service.FirebaseInstanceIdService
import com.levibostian.androidblanky.view.ui.activity.LaunchActivity
import com.levibostian.androidblanky.view.ui.activity.MainActivity
import com.levibostian.androidblanky.view.ui.fragment.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ServicesModule {

    @ContributesAndroidInjector
    abstract fun contributeFirebaseInstanceIdService(): FirebaseInstanceIdService

}