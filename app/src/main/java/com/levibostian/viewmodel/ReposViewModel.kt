package com.levibostian.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.levibostian.extensions.toLiveData
import com.levibostian.service.datasource.ReposTellerRepository
import com.levibostian.service.model.RepoModel
import com.levibostian.teller.cachestate.OnlineCacheState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReposViewModel @Inject constructor(private val reposRepository: ReposTellerRepository) : ViewModel() {

    fun setUsername(username: String) {
        reposRepository.requirements = ReposTellerRepository.GetRequirements(username)
    }

    fun observeRepos(): LiveData<OnlineCacheState<List<RepoModel>>> {
        return reposRepository.observe()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toLiveData()
    }
}
