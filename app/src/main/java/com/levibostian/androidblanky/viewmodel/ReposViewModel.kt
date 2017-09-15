package com.levibostian.androidblanky.viewmodel

import android.arch.lifecycle.*
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.repository.RepoRepository
import com.levibostian.androidblanky.service.statedata.StateData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.RealmResults

class ReposViewModel(val reposRepository: RepoRepository) : ViewModel() {

    fun getRepos(): Observable<StateData<RealmResults<RepoModel>>> {
        return reposRepository.getRepos()
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getReposUsername(): Observable<String> {
        return reposRepository.getReposUsername()
                .observeOn(AndroidSchedulers.mainThread())
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