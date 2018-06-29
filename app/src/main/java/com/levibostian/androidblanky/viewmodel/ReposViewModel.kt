package com.levibostian.androidblanky.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.teller.datastate.OnlineDataState
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReposViewModel(private val reposRepository: ReposRepository): ViewModel() {

    fun setUsername(username: String) {
        reposRepository.loadDataRequirements = ReposRepository.GetRequirements(username)
    }

    fun observeRepos(): LiveData<OnlineDataState<List<RepoModel>>> {
        return LiveDataReactiveStreams.fromPublisher(reposRepository.observe()
                .toFlowable(BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()))
    }

}