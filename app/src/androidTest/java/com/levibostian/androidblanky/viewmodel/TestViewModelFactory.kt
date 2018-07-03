package com.levibostian.androidblanky.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.testing.OpenForTesting

@OpenForTesting
class TestViewModelFactory(private val repoRepository: ReposRepository,
                           private val gitHubUsernameRepository: GitHubUsernameRepository) : ViewModelProvider.Factory {

    var models: List<ViewModel> = emptyList()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        for (model in models) {
            if (modelClass.isAssignableFrom(model::class.java)) {
                return model as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}