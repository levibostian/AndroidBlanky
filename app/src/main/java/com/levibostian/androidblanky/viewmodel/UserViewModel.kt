package com.levibostian.androidblanky.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.levibostian.androidblanky.extensions.toLiveData
import com.levibostian.androidblanky.service.GitHubService
import com.levibostian.androidblanky.service.db.Database
import com.levibostian.androidblanky.service.model.RepoModel
import com.levibostian.androidblanky.service.repository.ReposRepository
import com.levibostian.androidblanky.service.repository.UserRepository
import com.levibostian.androidblanky.service.vo.response.MessageResponseVo
import com.levibostian.androidblanky.testing.OpenForTesting
import com.levibostian.teller.cachestate.OnlineCacheState
import com.levibostian.teller.repository.OnlineRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@OpenForTesting
class UserViewModel(private val userRepository: UserRepository): ViewModel() {

    fun loginPasswordlessEmail(email: String): Single<OnlineRepository.FetchResponse<MessageResponseVo>> {
        return userRepository.loginPasswordlessEmail(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}