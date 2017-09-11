package com.levibostian.androidblanky.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.levibostian.androidblanky.service.repository.RepoRepository

class ViewModelFactory(val repoRepository: RepoRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReposViewModel::class.java)) {
            return ReposViewModel(repoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}