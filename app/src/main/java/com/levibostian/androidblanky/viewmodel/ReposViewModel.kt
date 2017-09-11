package com.levibostian.androidblanky.viewmodel

import android.app.Application
import android.arch.lifecycle.*
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.RealmInstanceWrapper
import com.levibostian.androidblanky.service.dao.repoDao
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.repository.RepoRepository
import com.levibostian.androidblanky.service.statedata.ReposStateData
import com.levibostian.androidblanky.service.statedata.StateData
import io.reactivex.Flowable
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmResults

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