package com.levibostian.androidblanky.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.service.repository.ReposRepository

class ViewModelFactory(private val repoRepository: ReposRepository,
                       private val gitHubUsernameRepository: GitHubUsernameRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReposViewModel::class.java)) {
            return ReposViewModel(repoRepository) as T
        }
        if (modelClass.isAssignableFrom(GitHubUsernameViewModel::class.java)) {
            return GitHubUsernameViewModel(gitHubUsernameRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}