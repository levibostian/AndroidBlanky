package com.levibostian.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.levibostian.service.repository.GitHubUsernameRepository
import com.levibostian.service.repository.ReposRepository
import com.levibostian.testing.OpenForTesting
import javax.inject.Inject
import javax.inject.Provider

@OpenForTesting
class TestViewModelFactory : ViewModelProvider.Factory {

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