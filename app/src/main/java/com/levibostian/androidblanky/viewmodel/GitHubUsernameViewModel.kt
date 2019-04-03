package com.levibostian.androidblanky.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.levibostian.androidblanky.extensions.toLiveData
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.androidblanky.testing.OpenForTesting
import com.levibostian.teller.cachestate.LocalCacheState
import io.reactivex.BackpressureStrategy

@OpenForTesting
class GitHubUsernameViewModel(private val repository: GitHubUsernameRepository): ViewModel() {

    private val repositoryRequirements = GitHubUsernameRepository.Requirements()

    fun setUsername(username: String) {
        repository.newCache(username, repositoryRequirements)
    }

    fun observeUsername(): LiveData<LocalCacheState<String>> {
        repository.requirements = repositoryRequirements
        return repository.observe().toLiveData()
    }

}