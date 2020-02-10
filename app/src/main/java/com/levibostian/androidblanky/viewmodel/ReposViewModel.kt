package com.levibostian.androidblanky.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.levibostian.androidblanky.extensions.toLiveData
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.testing.OpenForTesting
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