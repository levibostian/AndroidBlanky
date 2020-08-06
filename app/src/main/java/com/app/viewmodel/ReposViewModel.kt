package com.app.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.extensions.toLiveData
import com.app.service.datasource.ReposTellerRepository
import com.app.service.model.RepoModel
import com.levibostian.teller.cachestate.OnlineCacheState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReposViewModel @ViewModelInject constructor(private val reposRepository: ReposTellerRepository) : ViewModel() {

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
