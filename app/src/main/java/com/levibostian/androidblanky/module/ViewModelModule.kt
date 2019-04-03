package com.levibostian.androidblanky.module

import com.levibostian.androidblanky.viewmodel.GitHubUsernameViewModel
import com.levibostian.androidblanky.viewmodel.ReposViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object ViewModelModule {

    fun get(): Module {
        return module {
            viewModel { ReposViewModel(get()) }
            viewModel { GitHubUsernameViewModel(get()) }
        }
    }

}