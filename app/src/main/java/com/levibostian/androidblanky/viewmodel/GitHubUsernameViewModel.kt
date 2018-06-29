package com.levibostian.androidblanky.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.levibostian.androidblanky.service.repository.GitHubUsernameRepository
import com.levibostian.teller.datastate.LocalDataState
import io.reactivex.BackpressureStrategy

class GitHubUsernameViewModel(private val repository: GitHubUsernameRepository): ViewModel() {

    fun setUsername(username: String) {
        repository.saveData(username)
    }

    fun observeUsername(): LiveData<LocalDataState<String>> {
        return LiveDataReactiveStreams.fromPublisher(repository.observe().toFlowable(BackpressureStrategy.LATEST))
    }

}