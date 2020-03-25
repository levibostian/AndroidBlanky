package com.levibostian.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.levibostian.extensions.toLiveData
import com.levibostian.service.GitHubService
import com.levibostian.service.db.Database
import com.levibostian.service.model.RepoModel
import com.levibostian.service.repository.ReposRepository
import com.levibostian.testing.OpenForTesting
import com.levibostian.teller.cachestate.OnlineCacheState
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@OpenForTesting
class ReposViewModel @Inject constructor(private val reposRepository: ReposRepository): ViewModel() {

    fun setUsername(username: String) {
        reposRepository.requirements = ReposRepository.GetRequirements(username)
    }

    fun observeRepos(): LiveData<OnlineCacheState<List<RepoModel>>> {
        return reposRepository.observe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toLiveData()
    }

}