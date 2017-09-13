package com.levibostian.androidblanky.viewmodel

import android.arch.lifecycle.*
import com.levibostian.androidblanky.service.repository.RepoRepository
import com.levibostian.androidblanky.service.statedata.ReposStateData
import io.reactivex.Observable

class ReposViewModel(val reposRepository: RepoRepository) : ViewModel() {

    fun getRepos(): Observable<ReposStateData> {
        return reposRepository.getRepos()
    }

    fun getReposUsername(): Observable<String> {
        return reposRepository.getReposUsername()
    }

    override fun onCleared() {
        cleanup()
        super.onCleared()
    }

}

// Created for testing to access.
fun ReposViewModel.cleanup() {
    reposRepository.cleanup()
}