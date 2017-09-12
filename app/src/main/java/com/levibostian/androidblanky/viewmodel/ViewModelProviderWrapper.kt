package com.levibostian.androidblanky.viewmodel

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import com.levibostian.androidblanky.view.ui.fragment.SupportFragmentLifecycle

open class ViewModelProviderWrapper {

    fun <T: ViewModel> get(fragment: SupportFragmentLifecycle, clazz: Class<T>): T {
        return ViewModelProviders.of(fragment).get(clazz)
    }

}